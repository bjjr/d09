
package controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import domain.Actor;
import domain.MessageEntity;

@Controller
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ActorService	actorService;

	private boolean			forward;

	private boolean			reply;


	public MessageController() {
		super();
	}

	// Sending

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		MessageEntity messageEntity;

		messageEntity = this.messageService.create();
		Assert.notNull(messageEntity);
		result = this.createEditModelAndView(messageEntity);

		return result;
	}

	// Fowarding

	@RequestMapping(value = "/forward", method = RequestMethod.GET)
	public ModelAndView forward(@RequestParam final int messageId) {
		ModelAndView res;
		MessageEntity forward, messageEntity;

		messageEntity = this.messageService.findOne(messageId);
		forward = this.messageService.forwardMessage(messageEntity);
		this.forward = true;
		this.reply = false;

		res = this.createEditModelAndView(forward);

		return res;
	}

	// Replying

	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public ModelAndView reply(@RequestParam final int messageId) {
		ModelAndView res;
		MessageEntity reply, messageEntity;

		messageEntity = this.messageService.findOne(messageId);
		reply = this.messageService.replyMessage(messageEntity);
		this.reply = true;
		this.forward = false;

		res = this.createEditModelAndView(reply);

		return res;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "send")
	public ModelAndView save(final MessageEntity messageEntity, final BindingResult binding) {
		ModelAndView result;
		MessageEntity reconstructed;

		reconstructed = this.messageService.reconstruct(messageEntity, binding);
		this.forward = false;
		this.reply = false;

		if (binding.hasErrors())
			result = this.createEditModelAndView(messageEntity);
		else
			try {
				this.messageService.save(reconstructed);
				result = new ModelAndView("redirect:/message/listSent.do");
				result.addObject("message", "message.commit.ok");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageEntity, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "forward")
	public ModelAndView forward(final MessageEntity messageEntity, final BindingResult binding) {
		ModelAndView result;
		MessageEntity reconstructed;

		reconstructed = this.messageService.reconstruct(messageEntity, binding);
		this.forward = true;
		this.reply = false;

		if (binding.hasErrors())
			result = this.createEditModelAndView(messageEntity);
		else
			try {
				this.messageService.save(reconstructed);
				result = new ModelAndView("redirect:/message/listSent.do");
				result.addObject("message", "message.commit.ok");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageEntity, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "reply")
	public ModelAndView reply(final MessageEntity messageEntity, final BindingResult binding) {
		ModelAndView result;
		MessageEntity reconstructed;

		reconstructed = this.messageService.reconstruct(messageEntity, binding);
		this.reply = true;
		this.forward = false;

		if (binding.hasErrors())
			result = this.createEditModelAndView(messageEntity);
		else
			try {
				this.messageService.save(reconstructed);
				result = new ModelAndView("redirect:/message/listSent.do");
				result.addObject("message", "message.commit.ok");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageEntity, "message.commit.error");
			}

		return result;
	}

	// Deleting

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int messageId) {
		ModelAndView result;
		MessageEntity messageEntity;

		messageEntity = this.messageService.findOne(messageId);

		try {
			this.messageService.delete(messageEntity);
			result = new ModelAndView("redirect:/message/listSent.do");
			result.addObject("message", "message.commit.ok");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(messageEntity, "message.commit.error");
		}

		return result;
	}

	// Listing

	@RequestMapping(value = "/listSent", method = RequestMethod.GET)
	public ModelAndView listSent() {
		final ModelAndView res;
		Collection<MessageEntity> messageEntities;
		Boolean isReceivedView;

		messageEntities = this.messageService.findSentMessages();
		res = new ModelAndView("message/list");
		isReceivedView = false;

		res.addObject("requestURI", "message/listSent.do");
		res.addObject("messages", messageEntities);
		res.addObject("isReceivedView", isReceivedView);

		return res;
	}

	@RequestMapping(value = "/listReceived", method = RequestMethod.GET)
	public ModelAndView listReceived() {
		final ModelAndView res;
		Collection<MessageEntity> messageEntities;
		Boolean isReceivedView;

		messageEntities = this.messageService.findReceivedMessages();
		res = new ModelAndView("message/list");
		isReceivedView = true;

		res.addObject("requestURI", "message/listReceived.do");
		res.addObject("messages", messageEntities);
		res.addObject("isReceivedView", isReceivedView);

		return res;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final MessageEntity messageEntity) {
		ModelAndView result;

		result = this.createEditModelAndView(messageEntity, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageEntity messageEntity, final String message) {
		ModelAndView result;
		List<Actor> actors;

		actors = (List<Actor>) this.actorService.findAll();

		result = new ModelAndView("message/send");
		result.addObject("messageEntity", messageEntity);
		result.addObject("message", message);
		result.addObject("forward", this.forward);
		result.addObject("reply", this.reply);
		result.addObject("actors", actors);

		this.reply = false;
		this.forward = false;

		return result;
	}
}
