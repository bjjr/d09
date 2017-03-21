
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import domain.Actor;
import domain.Message;

@Controller
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;


	public MessageController() {
		super();
	}

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Message message;

		message = this.messageService.create();
		Assert.notNull(message);
		result = this.createEditModelAndView(message);

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "send")
	public ModelAndView save(final Message message, final BindingResult binding) {
		ModelAndView result;
		Message save;

		save = this.messageService.reconstruct(message, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(message);
		else
			try {
				this.messageService.save(save);
				result = new ModelAndView("redirect:/message/list.do");
				result.addObject("message", "message.commit.ok");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message, "message.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message actorMessage) {
		ModelAndView result;

		result = this.createEditModelAndView(actorMessage, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message actorMessage, final String message) {
		ModelAndView result;
		List<Actor> actors;
		final Boolean forward = false, reply = false;

		actors = (List<Actor>) this.actorService.findAll();

		result = new ModelAndView("message/send");
		result.addObject("actorMessage", actorMessage);
		result.addObject("message", message);
		result.addObject("forward", forward);
		result.addObject("reply", reply);
		result.addObject("actors", actors);

		return result;
	}
}
