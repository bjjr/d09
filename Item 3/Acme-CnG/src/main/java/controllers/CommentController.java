
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CommentService;
import domain.Actor;
import domain.Comment;
import domain.CommentableEntity;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	@Autowired
	private CommentService	commentService;

	@Autowired
	private ActorService	actorService;


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
		result.addObject("requestURI", "comment/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Comment comment;
		Actor principal;
		Collection<CommentableEntity> commentableEntities;

		principal = this.actorService.findByPrincipal();
		comment = this.commentService.create();
		result = new ModelAndView("comment/create");
		commentableEntities = this.commentService.commentableEntities(principal);

		result.addObject("comment", comment);
		result.addObject("commentableEntities", commentableEntities);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Comment comment, final BindingResult binding) {
		ModelAndView result;
		Collection<CommentableEntity> commentableEntities;

		comment = this.commentService.reconstruct(comment, binding);

		commentableEntities = this.commentService.commentableEntities(comment.getActor());

		if (binding.hasErrors()) {
			result = new ModelAndView();
			result.addObject("comment", comment);
			result.addObject("commentableEntities", commentableEntities);
		} else
			try {
				this.commentService.save(comment);
				result = new ModelAndView("redirect:../comment/list.do");
				result.addObject("messageStatus", "comment.commit.ok");
			} catch (final Throwable oops) {
				result = new ModelAndView();
				result.addObject("comment", comment);
				result.addObject("messageStatus", "comment.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/banComment", method = RequestMethod.GET)
	public ModelAndView banComment(@RequestParam final int commentId) {
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

}
