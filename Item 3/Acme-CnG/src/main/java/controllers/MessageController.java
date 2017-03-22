
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.MessageService;
import domain.Message;

@Controller
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageService	messageService;


	public MessageController() {
		super();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageId) {
		ModelAndView result;
		Message message;

		message = this.messageService.findOne(messageId);
		Assert.notNull(message);
		result = this.createEditModelAndView(message);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Message message, final BindingResult binding) {
		ModelAndView result;
		Message save;

		save = this.messageService.reconstruct(message, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(message);
		else
			try {
				this.messageService.save(save);
				result = new ModelAndView("redirect:/message/ownList.do");
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

		result = new ModelAndView("actorMessage/edit");
		result.addObject("actorMessage", actorMessage);
		result.addObject("message", message);

		return result;
	}

}
