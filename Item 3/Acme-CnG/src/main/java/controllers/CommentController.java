
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CommentService;
import domain.Actor;
import domain.Comment;

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
}
