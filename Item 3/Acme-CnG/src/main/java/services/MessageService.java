
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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


	// Constructor --------------------------------------------

	public MessageService() {
		super();
	}

	// Simple CRUD methods ------------------------------------

	public Message create(final int recipientId) {
		Message res;
		Collection<String> attachments;
		Date now;
		Actor sender, recipient;

		attachments = new ArrayList<String>();
		now = new Date(System.currentTimeMillis() - 1000);
		sender = this.actorService.findByPrincipal();
		recipient = this.actorService.findOne(recipientId);
		res = new Message();

		res.setMoment(now);
		res.setText("");
		res.setTitle("");
		res.setAttachments(attachments);
		res.setSender(sender);
		res.setRecipient(recipient);

		return res;
	}

	public Message save(final Message message) {
		Assert.notNull(message);
		Message res;

		res = this.messageRepository.save(message);

		return res;
	}

	/*
	 * Only the sender of a message can delete his/her message
	 */

	public void delete(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getSender().equals(this.actorService.findByPrincipal()));

		this.messageRepository.delete(message);
	}

	/*
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

	// Other business methods ---------------------------------

	public Collection<Message> findSentMessages() {
		Collection<Message> res;
		Actor sender;

		sender = this.actorService.findByPrincipal();

		res = this.messageRepository.findMessagesBySender(sender.getId());

		return res;
	}

	public Collection<Message> findReceivedMessages() {
		Collection<Message> res;
		Actor recipient;

		recipient = this.actorService.findByPrincipal();

		res = this.messageRepository.findMessagesByRecipient(recipient.getId());

		return res;
	}

	public Integer findMinNumSntMsgPerActor() {
		Integer res;

		res = this.messageRepository.findMinNumSntMsgPerActor().get(0);

		return res;
	}

	// TODO: Check this method

	public Double findAvgNumSntMsgPerActor() {
		Double res;

		res = this.messageRepository.findAvgNumSntMsgPerActor();

		return res;
	}

	public Integer findMaxNumSntMsgPerActor() {
		Integer res;

		res = this.messageRepository.findMinNumSntMsgPerActor().get(0);

		return res;
	}

	public Integer findMinNumRecMsgPerActor() {
		Integer res;

		res = this.messageRepository.findMinNumRecMsgPerActor().get(0);

		return res;
	}

	// TODO: Check this method

	public Double findAvgNumRecMsgPerActor() {
		Double res;

		res = this.messageRepository.findAvgNumRecMsgPerActor();

		return res;
	}

	public Integer findMaxNumRecMsgPerActor() {
		Integer res;

		res = this.messageRepository.findMaxNumRecMsgPerActor().get(0);

		return res;
	}

	public Collection<Actor> findActorsWithMoreSentMessages() {
		Collection<Actor> res;

		res = this.messageRepository.findActorsWithMoreSentMessages();

		return res;
	}

	public Collection<Actor> findActorsWithMoreReceivedMessages() {
		Collection<Actor> res;

		res = this.messageRepository.findActorsWithMoreReceivedMessages();

		return res;
	}
}
