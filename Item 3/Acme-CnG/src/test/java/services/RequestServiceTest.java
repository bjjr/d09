
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Place;
import domain.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private RequestService	requestService;


	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as a customer must be able to:
	 * Post a request in which he or she informs that he or she wishes to move from a place to another one and would like to find someone with whom he or she can share the trip
	 * Expected errors:
	 * - A non registered user tries to post a request --> IllegalArgumentException
	 * - An actor logged as administrator tries to post a request --> IllegalArgumentException
	 */

	@Test
	public void postRequestDriver() {
		final Object testingData[][] = {
			{
				null, IllegalArgumentException.class
			}, {
				"admin", IllegalArgumentException.class
			}, {
				"customer1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.postRequestTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * Use case: An actor who is authenticated as a customer must be able to:
	 * Search for requests using a single keyword that must appear somewhere in their titles, descriptions, or places
	 * Expected errors:
	 * - A non registered user tries to find requests by keyword --> IllegalArgumentException
	 * - An actor logged as administrator tries to find requests by keyword --> IllegalArgumentException
	 */

	@Test
	public void findRequestsByKeywordDriver() {
		final Object testingData[][] = {
			{
				null, "test", IllegalArgumentException.class
			}, {
				"admin", "test", IllegalArgumentException.class
			}, {
				"customer1", "", null
			}, {
				"customer2", "Hola", null
			}, {
				"customer3", "Title request 8", null
			}, {
				"customer2", "Description request 4", null
			}, {
				"customer1", "C/Place1Address", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.findRequestsByKeywordTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/*
	 * Use case: An actor who is authenticated as an administrator must be able to:
	 * Ban a request that he or she finds inappropriate
	 * Expected errors:
	 * - A non registered user tries to ban a request --> IllegalArgumentException
	 * - An actor logged as customer tries to ban a request --> IllegalArgumentException
	 */

	@Test
	public void banRequestDriver() {
		final Object testingData[][] = {
			{
				null, IllegalArgumentException.class
			}, {
				"customer1", IllegalArgumentException.class
			}, {
				"admin", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.banRequestTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	// Templates --------------------------------------------------------------

	protected void postRequestTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			final Place origin = new Place();
			final Place destination = new Place();
			Date moment = new Date();
			moment = DateUtils.addYears(moment, 2);
			origin.setAddress("Origin address test");
			destination.setAddress("Destination address test");
			final Request request = this.requestService.create();
			request.setTitle("Title test");
			request.setDescription("Description test");
			request.setMoment(moment);
			request.setOrigin(origin);
			request.setDestination(destination);
			this.requestService.save(request);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void findRequestsByKeywordTemplate(final String username, final String keyword, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			this.requestService.findByKeyword(keyword);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void banRequestTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			final Request request = this.requestService.findOne(131);
			this.requestService.ban(request);
			this.requestService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
