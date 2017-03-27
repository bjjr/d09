/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.MessageEntity;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private MessageService	messageService;

	// Services

	@Autowired
	private ActorService	actorService;


	// Tests ------------------------------------------------------------------

	/*
	 * Functional Requirement: Exchange messages with other actors.
	 * Expected errors:
	 * - A non registered user tries to send a message -> IllegalArgumentException
	 */

	@Test
	public void sendMessageDriver() {
		final Object testingData[][] = {
			{
				"customer1", 97, null
			// Login as customer1, send message to the admin, expect no exception
			}, {
				null, 97, IllegalArgumentException.class
			// Do not login, send message to the admin, expect IllegalArgumentException
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.sendMessageTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Functional Requirement: List the messages that he or she's got and reply to them.
	 * Expected errors:
	 * - A non registered user tries to list or reply to a message -> IllegalArgument Exception
	 */

	@Test
	public void listAndReplyMessageDriver() {
		final Object testingData[][] = {
			{
				"customer3", null
			// Login as customer3, expect no exception
			}, {
				null, IllegalArgumentException.class
			// Do not login, expect IllegalArgumentException
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listAndReplyMessageTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/*
	 * Functional Requirement: List the messages that he or she's got and forward them.
	 * Expected errors:
	 * - A user that did not received the original message tries to forward it -> IllegalArgumentException
	 */

	@Test
	public void listAndForwardMessageDriver() {
		final Object testingData[][] = {
			{
				"customer3", 108, 97, null
			// Login as customer3, forward message1 to the admin, expect no exception
			}, {
				"customer1", 108, 97, IllegalArgumentException.class
			// Login as customer1, forward message1 to the admin, expect IllegalArgumentException
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listAndForwardMessageTemplate((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/*
	 * Functional Requirement: Erase his or her messages, which requires previous confirmation.
	 * Expected errors:
	 * - A user tries to delete a message that is not his/hers -> IllegalArgumentException
	 */

	@Test
	public void deleteMessageDriver() {
		final Object testingData[][] = {
			{
				"customer3", 109, null
			// Login as customer3, delete message1, expect no exception
			}, {
				"customer1", 109, IllegalArgumentException.class
			// Login as customer1, delete message1 (customer1 has no messages sent), expect IllegalArgumentException
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteMessageTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void sendMessageTemplate(final String username, final int recipientId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			MessageEntity messageEntity, saved;
			Collection<MessageEntity> recipientMessages, senderMessages;
			Actor recipient;

			this.authenticate(username);

			recipient = this.actorService.findOne(recipientId);

			messageEntity = this.messageService.create();
			messageEntity.setTitle("test");
			messageEntity.setText("test");
			messageEntity.setRecipient(recipient);

			saved = this.messageService.save(messageEntity);
			this.messageService.flush();

			senderMessages = this.messageService.findSentMessages();

			Assert.isTrue(senderMessages.contains(saved));

			this.unauthenticate();

			this.authenticate("admin");

			recipientMessages = this.messageService.findReceivedMessages();

			Assert.isTrue(recipientMessages.contains(saved));

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void listAndReplyMessageTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			final MessageEntity message;
			MessageEntity saved, reply;
			final List<MessageEntity> receivedMessages;
			String recipientUsername;

			this.authenticate(username);

			receivedMessages = new ArrayList<MessageEntity>(this.messageService.findReceivedMessages());
			message = receivedMessages.get(0);

			reply = this.messageService.replyMessage(message);
			reply.setText("test");
			saved = this.messageService.save(reply);
			this.messageService.flush();

			this.unauthenticate();

			recipientUsername = saved.getRecipient().getUserAccount().getUsername();

			this.authenticate(recipientUsername);

			Assert.isTrue(this.messageService.findReceivedMessages().contains(saved));
			Assert.isTrue(saved.getTitle().contains("RE:"));

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void listAndForwardMessageTemplate(final String username, final int messageId, final int actorId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			final MessageEntity message;
			MessageEntity saved, forwarded;
			Actor admin;

			this.authenticate(username);

			message = this.messageService.findOne(messageId);
			admin = this.actorService.findOne(actorId);

			forwarded = this.messageService.forwardMessage(message);
			forwarded.setRecipient(admin);

			saved = this.messageService.save(forwarded);
			this.messageService.flush();

			this.unauthenticate();

			this.authenticate("admin");

			Assert.isTrue(this.messageService.findReceivedMessages().contains(saved));
			Assert.isTrue(saved.getTitle().contains("FW:"));

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteMessageTemplate(final String username, final int messageId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			final MessageEntity message;

			this.authenticate(username);

			message = this.messageService.findOne(messageId);

			this.messageService.delete(message);
			this.messageService.flush();

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
