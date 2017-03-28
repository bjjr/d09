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
import domain.Application;
import domain.Customer;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ApplicationService	applicationService;

	// Services ------------------------------------------------------

	@Autowired
	private CustomerService		customerService;


	// Tests ------------------------------------------------------------------

	/*
	 * Use case: An actor who is authenticated as a customer must be able to:
	 * Apply for an offer or a request
	 * Expected errors:
	 * - A customer tries to apply for an owned trip --> IllegalArgumentException
	 * - A non registered user tries to create an application --> IllegalArgumentException
	 * - An administrator tries to create an application --> IllegalArgumentException
	 * - A customer tries to apply for a trip already applied --> IllegalArgumentException
	 */

	@Test
	public void createApplicationDriver() {
		final Object testingData[][] = {
			{    // A customer cannot apply for an owned trip
				"customer2", 115, IllegalArgumentException.class
			}, { // A non registered user cannot create an application
				null, 115, IllegalArgumentException.class
			}, { // An administrator cannot create an application
				"admin", 115, IllegalArgumentException.class
			}, { // A customer cannot apply for a trip already applied
				"customer2", 116, IllegalArgumentException.class
			}, { // Successful test
				"customer2", 117, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createApplicationTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: An actor who is authenticated as a customer must be able to:
	 * Accept an application that other customer applied for one of his/her trips
	 * Expected errors:
	 * - That user tries to accept some accepted or denied application --> IllegalArgumentException
	 * - That user tries to accept some application that not belongs to him/her --> IllegalArgumentException
	 * - A non registered user tries to accept an application --> IllegalArgumentException
	 * - An administrator tries to accept an application --> IllegalArgumentException
	 */

	@Test
	public void acceptApplicationDriver() {
		final Object testingData[][] = {
			{    // Successful test
				"customer7", 149, null
			}, { // Cannot accept an accepted/denied application
				"customer3", 137, IllegalArgumentException.class
			}, { // Cannot accept an application that not belongs to him/her
				"customer1", 136, IllegalArgumentException.class
			}, { // A non registered user cannot accept an application
				null, 136, IllegalArgumentException.class
			}, { // An administrator cannot accept an application
				"admin", 136, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptApplicationTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: An actor who is authenticated as a customer must be able to:
	 * Deny an application that other customer applied for one of his/her trips
	 * Expected errors:
	 * - That user tries to deny some accepted or denied application --> IllegalArgumentException
	 * - That user tries to deny some application that not belongs to him/her --> IllegalArgumentException
	 * - A non registered user tries to deny an application --> IllegalArgumentException
	 * - An administrator tries to deny an application --> IllegalArgumentException
	 */

	@Test
	public void denyApplicationDriver() {
		final Object testingData[][] = {
			{    // Successful test
				"customer7", 149, null
			}, { // Cannot deny an accepted/denied application
				"customer3", 143, IllegalArgumentException.class
			}, { // Cannot deny an application that not belongs to him/her
				"customer1", 136, IllegalArgumentException.class
			}, { // A non registered user cannot deny an application
				null, 136, IllegalArgumentException.class
			}, { // An administrator cannot deny an application
				"admin", 136, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.denyApplicationTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Checks if the query findAvgApplicationsPerOffer works
	 */

	@Test
	public void testFindAvgApplicationsPerOffer() {
		Double avg;

		this.authenticate("admin");

		avg = this.applicationService.findAvgApplicationsPerOffer();

		Assert.isTrue(avg.equals(1.0));

		this.unauthenticate();

	}

	/*
	 * Checks if the query findAvgApplicationsPerRequest works
	 */

	@Test
	public void testFindAvgApplicationsPerRequest() {
		Double avg;

		this.authenticate("admin");

		avg = this.applicationService.findAvgApplicationsPerRequest();

		Assert.isTrue(avg.equals(1.33333));

		this.unauthenticate();

	}

	/*
	 * Use case: An actor who is authenticated as a customer must be able to:
	 * Apply for an offer or a request, which must be accepted by the customer who posted it
	 * Expected errors:
	 * - A non registered user tries to apply for a trip --> IllegalArgumentException
	 * - An administrator tries to apply for a trip --> IllegalArgumentException
	 * - A non registered user tries to accept an application --> IllegalArgumentException
	 * - An administrator tries to accept an application --> IllegalArgumentException
	 * - A customer tries to accept an application that not belongs to him/her --> IllegalArgumentException
	 * - A customer tries to apply for a trip already applied by him/her --> IllegalArgumentException
	 * - A customer tries to apply for an owned trip --> IllegalArgumentException
	 * - A customer tries to accept an application applied by him/her --> IllegalArgumentException
	 */

	@Test
	public void applyForATripDriver() {
		final Object testingData[][] = {
			{    // An actor non registered cannot apply for a trip
				null, "customer2", 123, IllegalArgumentException.class
			}, { // An administrator cannot apply for a trip
				"admin", "customer2", 123, IllegalArgumentException.class
			}, { // An actor non registered cannot accept and application
				"customer1", null, 123, IllegalArgumentException.class
			}, { // An administrator cannot accept and application
				"customer1", "admin", 123, IllegalArgumentException.class
			}, { // A customer cannot accept an application that not belongs to him/her
				"customer1", "customer2", 119, IllegalArgumentException.class
			}, { // A customer cannot apply for a trip already applied by him/her
				"customer3", "customer4", 120, IllegalArgumentException.class
			}, { // A customer cannot apply for an owned offer
				"customer3", "customer4", 117, IllegalArgumentException.class
			}, { // A customer cannot accept an application applied by him/her
				"customer1", "customer1", 130, IllegalArgumentException.class
			}, { // Successful test
				"customer1", "customer4", 122, null
			}, { // Successful test
				"customer1", "customer7", 128, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.applyForATripTemplate((String) testingData[i][0], (String) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	// Ancillary methods ------------------------------------------------------

	protected void acceptApplicationTemplate(final String username, final int applicationId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Application app;

			this.authenticate(username);

			app = this.applicationService.findOne(applicationId);
			this.applicationService.accept(app);
			this.applicationService.flush();

			this.unauthenticate();

			Assert.isTrue(app.getStatus() == "ACCEPTED");
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void denyApplicationTemplate(final String username, final int applicationId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Application app;

			this.authenticate(username);

			app = this.applicationService.findOne(applicationId);
			this.applicationService.deny(app);
			this.applicationService.flush();

			this.unauthenticate();

			Assert.isTrue(app.getStatus() == "DENIED");
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void createApplicationTemplate(final String username, final int tripId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Application app, saved;
			Collection<Application> apps;
			final Customer principal;
			int id;

			this.authenticate(username);

			app = this.applicationService.create();
			principal = this.customerService.findByPrincipal();
			id = principal.getId();

			saved = this.applicationService.save(app, tripId);
			this.applicationService.flush();
			apps = this.applicationService.findApplicationsByCustomer(id);

			Assert.isTrue(apps.contains(saved));

			this.unauthenticate();

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void applyForATripTemplate(final String username1, final String username2, final int tripId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Application app, saved;
			Collection<Application> applications;

			this.authenticate(username1);

			app = this.applicationService.create();
			saved = this.applicationService.save(app, tripId);
			this.applicationService.flush();

			this.unauthenticate();

			this.authenticate(username2);
			Customer principal;

			principal = this.customerService.findByPrincipal();
			applications = this.applicationService.findApplicationsAllTripsOfCustomer(principal);
			Assert.isTrue(applications.contains(saved));
			this.applicationService.accept(saved);

			this.unauthenticate();

			this.authenticate(username1);

			Assert.isTrue(saved.getStatus() == "ACCEPTED");

			this.unauthenticate();

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
