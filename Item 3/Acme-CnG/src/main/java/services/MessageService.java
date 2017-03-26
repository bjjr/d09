
package services;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.validation.Payload;
import javax.validation.constraints.Pattern.Flag;

import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.internal.constraintvalidators.URLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.MessageEntity;

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

	public MessageEntity create() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));

		MessageEntity res;
		Collection<String> attachments;
		Actor sender;

		attachments = new ArrayList<String>();
		sender = this.actorService.findByPrincipal();
		res = new MessageEntity();

		res.setText("");
		res.setTitle("");
		res.setAttachments(attachments);
		res.setSender(sender);

		return res;
	}

	public MessageEntity save(final MessageEntity messageEntity) {
		Assert.notNull(messageEntity);
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));

		MessageEntity res;
		Date now;

		now = new Date(System.currentTimeMillis() - 1000);
		messageEntity.setMoment(now);
		res = this.messageRepository.save(messageEntity);

		return res;
	}

	/*
	 * Only the sender of a message can delete his/her message
	 */

	public void delete(final MessageEntity messageEntity) {
		Actor actor;
		Assert.notNull(messageEntity);
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));

		actor = this.actorService.findByPrincipal();

		Assert.isTrue(messageEntity.getSender().equals(actor));

		this.messageRepository.delete(messageEntity.getId());
	}

	/*
	 * Users can only access messages related to them
	 */

	public MessageEntity findOne(final int messageId) {
		Assert.isTrue(messageId != 0);

		MessageEntity res;

		res = this.messageRepository.findOne(messageId);

		Assert.notNull(res, "The message does not exist");
		Assert.isTrue(res.getSender().equals(this.actorService.findByPrincipal()) || res.getRecipient().equals(this.actorService.findByPrincipal()));

		return res;
	}

	public MessageEntity reconstruct(final MessageEntity messageEntity, final BindingResult bindingResult) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		MessageEntity result;
		Actor actor;

		result = messageEntity;
		actor = this.actorService.findByPrincipal();
		result.setSender(actor);
		result.setMoment(new Date(System.currentTimeMillis()));

		this.validateURLs(result.getAttachments(), bindingResult);
		this.validator.validate(result, bindingResult);

		return result;
	}

	private void validateURLs(final Collection<String> attachments, final BindingResult binding) {
		URLValidator validator;

		validator = new URLValidator();

		validator.initialize(new URL() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String regexp() {
				return ".*";
			}

			@Override
			public String protocol() {
				return "";
			}

			@Override
			public int port() {
				return -1;
			}

			@Override
			public Class<? extends Payload>[] payload() {
				return null;
			}

			@Override
			public String message() {
				return "org.hibernate.validator.constraints.URL.message";
			}

			@Override
			public String host() {
				return "";
			}

			@Override
			public Class<?>[] groups() {
				return null;
			}

			@Override
			public Flag[] flags() {
				return null;
			}
		});

		for (final String s : attachments)
			if (!validator.isValid(s, null)) {
				binding.rejectValue("attachments", "org.hibernate.validator.constraints.URL.message");
				break;
			}
	}

	// Other business methods ---------------------------------

	public MessageEntity forwardMessage(final MessageEntity messageEntity) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		Assert.notNull(messageEntity);

		MessageEntity forwarded;

		forwarded = this.create();
		forwarded.setTitle("FW: " + messageEntity.getTitle());
		forwarded.setText(messageEntity.getText());
		forwarded.setAttachments(messageEntity.getAttachments());

		return forwarded;
	}

	public MessageEntity replyMessage(final MessageEntity messageEntity) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		Assert.notNull(messageEntity);

		MessageEntity reply;

		reply = this.create();
		reply.setTitle("RE: " + messageEntity.getTitle());
		reply.setRecipient(messageEntity.getSender());

		return reply;
	}

	public Collection<MessageEntity> findSentMessages() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		Collection<MessageEntity> res;
		Actor sender;

		sender = this.actorService.findByPrincipal();
		res = this.messageRepository.findMessagesBySender(sender.getId());

		return res;
	}

	public Collection<MessageEntity> findReceivedMessages() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
		Collection<MessageEntity> res;
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
