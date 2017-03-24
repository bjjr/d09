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
import domain.Comment;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CommentServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private CommentService				commentService;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private CommentableEntityService	commentableEntityService;

	@Autowired
	private CustomerService				customerService;


	// Services

	// Tests ------------------------------------------------------------------

	/*
	 * Use case: A registered user creates and sends a message to another user
	 * Expected errors:
	 * - A non registered user tries to send a message -> IllegalArgumentException
	 */

	@Test
	public void saveBannerDriver() {
		final Object testingData[][] = {
			{
				"admin", "Sample", "Sample", 5, false, 99, null
			}, {
				null, "Sample", "Sample", 5, false, 99, IllegalArgumentException.class
			}, {
				"customer1", "Sample", "Sample", 7, false, 99, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.saveCommentTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3], (boolean) testingData[i][4], (int) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	// Ancillary methods ------------------------------------------------------

	protected void saveCommentTemplate(final String username, final String title, final String text, final int stars, final boolean banned, final int commentableEntityId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			final Comment comment, saved;
			final Collection<Comment> comments;

			this.unauthenticate();
			this.authenticate(username);

			comment = new Comment();
			comment.setCommentableEntity(this.customerService.findOne(commentableEntityId));
			comment.setTitle(title);
			comment.setText(text);
			comment.setStars(stars);
			comment.setBanned(banned);

			saved = this.commentService.save(comment);

			Assert.isTrue(title.equals(saved.getTitle()), "The saved Title doesnt correspond with the proposed Title change");

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
