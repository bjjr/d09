
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.MessageRepository;
import domain.Actor;
import domain.Customer;
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
	private CustomerService		customerService;


	// Constructor --------------------------------------------

	public MessageService() {
		super();
	}

	// Simple CRUD methods ------------------------------------

	public Message create(final int recipientId) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));

		Message res;
		Collection<String> attachments;
		Actor sender, recipient;

		attachments = new ArrayList<String>();
		sender = this.actorService.findByPrincipal();
		recipient = this.actorService.findOne(recipientId);
		res = new Message();

		res.setText("");
		res.setTitle("");
		res.setAttachments(attachments);
		res.setSender(sender);
		res.setRecipient(recipient);

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
	 * Only the sender of a message can delete his/her message
	 */

	public void delete(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));
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

	public Message reconstruct(final Message property, final BindingResult bindingResult) {
		Message result;

		if (property.getId() == 0) {
			Customer customer;

			result = property;
			customer = this.customerService.findByPrincipal();

			result.setLessor(customer);
			result.setAudits(audits);
			result.setBooks(books);

		} else {
			Message aux;
			aux = propertyRepository.findOne(property.getId());

			result = property;

			result.setAudits(aux.getAudits());
			result.setBooks(aux.getBooks());
			result.setLessor(aux.getLessor());

		}
		validator.validate(result, bindingResult);

		return result;

	}

	// Other business methods ---------------------------------

	public void forwardMessage(final Message message, final int recipientId) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") && this.actorService.checkAuthority("CUSTOMER"));
		Assert.notNull(message);

		Message forwarded;

		forwarded = this.create(recipientId);

		forwarded.setTitle("FW: " + message.getTitle());
		forwarded.setText(message.getText());
		forwarded.setAttachments(message.getAttachments());

		this.save(message);
	}

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
