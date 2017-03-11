
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

}
