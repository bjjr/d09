
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.hibernate.validator.internal.constraintvalidators.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Message;

@Service
@Transactional
public class MessageService {

	// Managed repository -------------------------------------

	@Autowired
	private MessageRepository	messageRepository;

	// Supporting services ------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	// Constructor --------------------------------------------

	public MessageService() {

		super();

	}

	// Simple CRUD methods ------------------------------------

	public Message create() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));

		Message res;
		Collection<String> attachments;
		Actor sender;

		attachments = new ArrayList<String>();
		sender = this.actorService.findByPrincipal();
		res = new Message();

		res.setText("");
		res.setTitle("");
		res.setAttachments(attachments);
		res.setSender(sender);

		return res;
	}

	public Message save(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));

		Message res;
		Date now;

		now = new Date(System.currentTimeMillis() - 1000);
		message.setMoment(now);
		res = this.messageRepository.save(message);

		return res;
	}

	/*
	 * 
	 * Only the sender of a message can delete his/her message
	 */

	public void delete(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		Assert.isTrue(message.getSender().equals(this.actorService.findByPrincipal()));

		this.messageRepository.delete(message);
	}

	/*
	 * 
	 * Users can only access messages related to them
	 */

	public Message findOne(final int messageId) {
		Assert.isTrue(messageId != 0);

		Message res;

		res = this.messageRepository.findOne(messageId);

		Assert.notNull(res, "The message does not exist");
		Assert.isTrue(res.getSender().equals(this.actorService.findByPrincipal()) || res.getRecipient().equals(this.actorService.findByPrincipal()));

		return res;
	}

	public Message reconstruct(final Message message, final BindingResult bindingResult) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		Message result;
		Actor actor;

		result = message;
		actor = this.actorService.findByPrincipal();
		result.setSender(actor);

		this.validateURLs(result.getAttachments(), bindingResult);
		this.validator.validate(result, bindingResult);

		return result;
	}

	private void validateURLs(final Collection<String> attachments, final BindingResult binding) {
		URLValidator validator;

		validator = new URLValidator();

		for (final String s : attachments)
			if (!validator.isValid(s, null)) {
				binding.rejectValue("attachments", "org.hibernate.validator.constraints.URL.message");
				break;
			}
	}

	// Other business methods ---------------------------------

	public Message forwardMessage(final Message message) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		Assert.notNull(message);

		Message forwarded;

		forwarded = this.create();
		forwarded.setTitle("FW: " + message.getTitle());
		forwarded.setText(message.getText());
		forwarded.setAttachments(message.getAttachments());

		return forwarded;
	}

	public Collection<Message> findSentMessages() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		Collection<Message> res;
		Actor sender;

		sender = this.actorService.findByPrincipal();
		res = this.messageRepository.findMessagesBySender(sender.getId());

		return res;
	}

	public Collection<Message> findReceivedMessages() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		Collection<Message> res;
		Actor recipient;

		recipient = this.actorService.findByPrincipal();
		res = this.messageRepository.findMessagesByRecipient(recipient.getId());

		return res;
	}

	public Long findMinNumSntMsgPerActor() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));
		Long res;

		res = 0L;

		if (!this.messageRepository.findMinNumSntMsgPerActor().isEmpty())
			res = this.messageRepository.findMinNumSntMsgPerActor().get(0);

		return res;
	}

	public Double findAvgNumSntMsgPerActor() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));
		Double res;

		res = this.messageRepository.findAvgNumSntMsgPerActor();

		return res;
	}

	public Long findMaxNumSntMsgPerActor() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));
		Long res;

		res = 0L;

		if (!this.messageRepository.findMaxNumSntMsgPerActor().isEmpty())
			res = this.messageRepository.findMaxNumSntMsgPerActor().get(0);

		return res;
	}

	public Long findMinNumRecMsgPerActor() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));
		Long res;

		res = 0L;

		if (!this.messageRepository.findMinNumRecMsgPerActor().isEmpty())
			res = this.messageRepository.findMinNumRecMsgPerActor().get(0);

		return res;
	}

	public Double findAvgNumRecMsgPerActor() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));
		Double res;

		res = this.messageRepository.findAvgNumRecMsgPerActor();

		return res;
	}

	public Long findMaxNumRecMsgPerActor() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));
		Long res;

		res = 0L;

		if (!this.messageRepository.findMaxNumRecMsgPerActor().isEmpty())
			res = this.messageRepository.findMaxNumRecMsgPerActor().get(0);

		return res;
	}

}
