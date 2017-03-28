
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CommentService;
import services.TripService;
import domain.Actor;
import domain.Comment;
import domain.CommentableEntity;
import domain.Trip;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	@Autowired
	private CommentService	commentService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private TripService		tripService;

	private int				tripId;


	// Listing -----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Comment> comments;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		comments = this.commentService.findCommentsByCommentableEntity(principal.getId());

		result = new ModelAndView("comment/list");
		result.addObject("comments", comments);
		result.addObject("tripId", null);
		result.addObject("requestURI", "comment/list.do");

		return result;
	}

	@RequestMapping(value = "/listTrip", method = RequestMethod.GET)
	public ModelAndView listTrip(@RequestParam final int tripId) {
		ModelAndView result;
		Collection<Comment> comments;
		final Trip trip;

		trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);
		comments = this.commentService.findCommentsByCommentableEntity(trip.getId());

		result = new ModelAndView("comment/listTrip");
		result.addObject("comments", comments);
		result.addObject("tripId", tripId);
		result.addObject("requestURI", "comment/list.do");

		return result;
	}

	@RequestMapping(value = "/listActor", method = RequestMethod.GET)
	public ModelAndView listTrip() {
		ModelAndView result;
		Collection<Comment> comments;

		comments = this.commentService.findCommentsOnActors();

		result = new ModelAndView("comment/listTrip");
		result.addObject("comments", comments);
		result.addObject("requestURI", "comment/listActor.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Comment comment;
		Collection<Actor> commentableEntities;

		comment = this.commentService.create();
		result = new ModelAndView("comment/create");
		commentableEntities = this.actorService.findAll();

		result.addObject("comment", comment);
		result.addObject("isTrip", false);
		result.addObject("formAction", "comment/create.do");
		result.addObject("commentableEntities", commentableEntities);

		return result;
	}

	@RequestMapping(value = "/createTrip", method = RequestMethod.GET)
	public ModelAndView createTrip(@RequestParam final int tripId) {
		ModelAndView result;
		Comment comment;
		final Trip trip;

		trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);
		this.setTripId(tripId);

		comment = this.commentService.create();
		result = new ModelAndView("comment/create");

		result.addObject("comment", comment);
		result.addObject("formAction", "comment/createTrip.do");
		result.addObject("isTrip", true);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAsTrip(Comment comment, final BindingResult binding) {
		ModelAndView result;
		Collection<CommentableEntity> commentableEntities;

		comment = this.commentService.reconstruct(comment, binding);

		commentableEntities = this.commentService.commentableEntities(comment.getActor());

		if (binding.hasErrors()) {
			result = new ModelAndView();
			result.addObject("comment", comment);
			result.addObject("isTrip", false);
			result.addObject("commentableEntities", commentableEntities);
		} else
			try {
				this.commentService.save(comment);
				result = new ModelAndView("redirect:../comment/list.do");
				result.addObject("messageStatus", "comment.commit.ok");
			} catch (final Throwable oops) {
				result = new ModelAndView();
				result.addObject("comment", comment);
				result.addObject("isTrip", false);
				result.addObject("messageStatus", "comment.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/createTrip", method = RequestMethod.POST, params = "saveAsTrip")
	public ModelAndView save(Comment comment, final BindingResult binding) {
		ModelAndView result;

		comment = this.commentService.reconstruct(comment, this.getTripId(), binding);

		if (binding.hasErrors()) {
			result = new ModelAndView();
			result.addObject("comment", comment);
			result.addObject("isTrip", true);
		} else
			try {
				this.commentService.save(comment);
				result = new ModelAndView("redirect:../comment/listTrip.do?tripId=" + this.getTripId());
				result.addObject("messageStatus", "comment.commit.ok");
			} catch (final Throwable oops) {
				result = new ModelAndView();
				result.addObject("comment", comment);
				result.addObject("isTrip", true);
				result.addObject("messageStatus", "comment.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int commentId) {
		ModelAndView result;

		try {
			this.commentService.banComment(commentId);
			result = new ModelAndView("redirect:list.do");
		} catch (final IllegalArgumentException oops) {
			result = this.list();
			result.addObject("message", "comment.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/banTrip", method = RequestMethod.GET)
	public ModelAndView banTrip(@RequestParam final int tripId, @RequestParam final int commentId) {
		ModelAndView result;

		try {
			this.commentService.banComment(commentId);
			result = new ModelAndView("redirect:listTrip.do?tripId=" + tripId);
		} catch (final IllegalArgumentException oops) {
			result = this.list();
			result.addObject("message", "comment.commit.error");
		}
		return result;
	}

	public int getTripId() {
		return this.tripId;
	}

	public void setTripId(final int tripId) {
		this.tripId = tripId;
	}

}
