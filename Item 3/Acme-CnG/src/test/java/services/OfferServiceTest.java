
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Offer;
import domain.Place;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class OfferServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private OfferService	offerService;


	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as a customer must be able to:
	 * Post an offer in which he or she advertises that he's going to move from a place to another place and would like to share his or her car with someone else
	 * Expected errors:
	 * - A non registered user tries to post an offer --> IllegalArgumentException
	 * - An actor logged as administrator tries to post an offer --> IllegalArgumentException
	 */

	@Test
	public void postOfferDriver() {
		final Object testingData[][] = {
			{    //An actor unauthenticated cannot post offers
				null, IllegalArgumentException.class
			}, { //An admin cannot post offers
				"admin", IllegalArgumentException.class
			}, { // Successful test
				"customer1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.postOfferTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * Use case: An actor who is authenticated as a customer must be able to:
	 * Search for offers using a single keyword that must appear somewhere in their titles, descriptions, or places
	 * Expected errors:
	 * - A non registered user tries to find offers by keyword --> IllegalArgumentException
	 * - An actor logged as administrator tries to find offers by keyword --> IllegalArgumentException
	 */

	@Test
	public void findOffersByKeywordDriver() {
		final Object testingData[][] = {
			{    // An actor unauthenticated cannot find offers by a keyword
				null, "test", IllegalArgumentException.class
			}, { // An admin cannot find offers by a keyword
				"admin", "test", IllegalArgumentException.class
			}, { // Successful test with all offers found
				"customer1", "", null
			}, { // Successful test without offers found
				"customer2", "Hola", null
			}, { // Successful test with offers found by a title
				"customer3", "Title offer 10", null
			}, { // Successful test with offers found by a description
				"customer2", "Description offer 2", null
			}, { // Successful test with offers found by an address of an origin or a destination
				"customer1", "C/Place1Address", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.findOffersByKeywordTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	/*
	 * Use case: An actor who is authenticated as an administrator must be able to:
	 * Ban an offer that he or she finds inappropriate
	 * Expected errors:
	 * - A non registered user tries to ban an offer --> IllegalArgumentException
	 * - An actor logged as customer tries to ban an offer --> IllegalArgumentException
	 */

	@Test
	public void banOfferDriver() {
		final Object testingData[][] = {
			{    // An actor unauthenticated cannot ban offers
				null, IllegalArgumentException.class
			}, { // A customer cannot ban offers
				"customer1", IllegalArgumentException.class
			}, { // Successful test
				"admin", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.banOfferTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * Checks if the query findAvgOfferPerCustomer works
	 */

	@Test
	public void testFindAvgOfferPerCustomer() {
		this.authenticate("admin");

		Double avg;

		avg = this.offerService.findAvgOfferPerCostumer();
		Assert.isTrue(avg.equals(1.33333));

		this.unauthenticate();

	}

	// Templates --------------------------------------------------------------

	protected void postOfferTemplate(final String username, final Class<?> expected) {
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
			final Offer offer = this.offerService.create();
			offer.setTitle("Title test");
			offer.setDescription("Description test");
			offer.setMoment(moment);
			offer.setOrigin(origin);
			offer.setDestination(destination);
			final Offer offerSaved = this.offerService.save(offer);
			this.offerService.flush();

			this.unauthenticate();

			Assert.isTrue(this.offerService.findAll().contains(offerSaved));
			Assert.isTrue(!offerSaved.isBanned());
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void findOffersByKeywordTemplate(final String username, final String keyword, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			this.offerService.findByKeyword(keyword);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void banOfferTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			final Offer offer = this.offerService.findOne(118);
			this.offerService.ban(offer);
			this.offerService.flush();
			this.unauthenticate();

			Assert.isTrue(offer.isBanned());
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
