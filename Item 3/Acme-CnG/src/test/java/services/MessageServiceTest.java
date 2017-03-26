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

import java.util.Collection;

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
	 * Use case: A registered user creates and sends a message to another user
	 * Expected errors:
	 * - A non registered user tries to send a message -> IllegalArgumentException
	 */

	@Test
	public void sendMessageDriver() {
		final Object testingData[][] = {
			{
				"customer1", 97, null
			}, {
				null, 97, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.sendMessageTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
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
}
