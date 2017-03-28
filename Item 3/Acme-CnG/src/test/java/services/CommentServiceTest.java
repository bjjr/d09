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

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
	private CommentService	commentService;

	@Autowired
	private CustomerService	customerService;


	// Services

	// Tests ------------------------------------------------------------------

	/*
	 * Use case: A registered user creates a new comment to a CommentableEntity
	 * Expected errors:
	 * - A registered user tries to write an empty title -> ConstraintViolationException
	 * - A registered user tries to write an empty text -> ConstraintViolationException
	 * - A registered user tries to write a comment with invalid stars -> ConstraintViolationException
	 * - A non registered user tries to write a new comment -> IllegalArgumentException
	 */

	@Test
	public void saveCommentDriver() {
		final Object testingData[][] = {
			{
				"admin", "Sample", "Sample", 5, false, 99, null
			}, {
				"customer1", "Sample", "Sample", 0, false, 99, null
			}, {
				"customer1", "Sample", "Sample", 1, false, 99, null
			}, {
				null, "Sample", "Sample", 5, false, 99, IllegalArgumentException.class
			}, {
				"customer1", "Sample", "Sample", 7, false, 99, ConstraintViolationException.class
			}, {
				"customer1", "", "Sample", 7, false, 99, ConstraintViolationException.class
			}, {
				"customer1", "Sample", "", 7, false, 99, ConstraintViolationException.class
			}, {
				"customer1", "", "", 7, false, 99, ConstraintViolationException.class
			}, {
				"customer1", "", "", -1, false, 99, ConstraintViolationException.class
			}, {
				"customer1", "", "", 0, false, 99, ConstraintViolationException.class
			}, {
				"customer1", "", "", 5, false, 99, ConstraintViolationException.class
			}, {
				"customer1", "", "", 6, false, 99, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.saveCommentTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (int) testingData[i][3], (boolean) testingData[i][4], (int) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/*
	 * Use case: An admin user ban a posted comment
	 * Expected errors:
	 * - A Customer user tries to ban a comment -> IllegalArgumentException
	 * - A non registered user tries to ban a comment -> IllegalArgumentException
	 */

	@Test
	public void banCommentDriver() {
		final Object testingData[][] = {
			{
				"admin", 165, null
			}, {
				"customer1", 165, IllegalArgumentException.class
			}, {
				null, 165, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.banCommentTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: Find comments by Actor/Trip
	 * Expected errors:
	 * - A non registered user list the comments -> IllegalArgumentException
	 */

	@Test
	public void listCommentsDriver() {
		final Object testingData[][] = {
			{
				"admin", 118, null
			}, {
				"customer1", 120, null
			}, {
				null, 118, IllegalArgumentException.class
			}, {
				null, 120, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listCommentsTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void saveCommentTemplate(final String username, final String title, final String text, final int stars, final boolean banned, final int commentableEntityId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			final Comment comment;

			this.unauthenticate();
			this.authenticate(username);

			comment = new Comment();
			comment.setCommentableEntity(this.customerService.findOne(commentableEntityId));
			comment.setTitle(title);
			comment.setText(text);
			comment.setStars(stars);
			comment.setBanned(banned);

			this.commentService.save(comment);
			this.commentService.flush();

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void banCommentTemplate(final String username, final int commentId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.unauthenticate();
			this.authenticate(username);

			this.commentService.banComment(commentId);

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void listCommentsTemplate(final String username, final int commentableEntityId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.unauthenticate();
			this.authenticate(username);

			this.commentService.findCommentsByCommentableEntity(commentableEntityId);

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
