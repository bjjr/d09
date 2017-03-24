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
import domain.Trip;

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
	private TripService			tripService;

	@Autowired
	private CustomerService		customerService;


	// Tests ------------------------------------------------------------------

	/*
	 * Use case: A registered customer creates an application
	 * Expected errors:
	 * - A non registered user tries to create an application -> IllegalArgumentException
	 * - An administrator tries to create an application -> IllegalArgumentException
	 */

	@Test
	public void createApplicationDriver() {
		final Object testingData[][] = {
			{
				"customer2", 115, null
			}, {
				null, 115, IllegalArgumentException.class
			}, {
				"admin", 115, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createApplicationTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: A registered customer that owns an offer or a request accepts an
	 * application of that offer or request
	 * Expected errors:
	 * - That user tries to accept some accepted or denied application -> IllegalArgumentException
	 * - That user tries to accept some application that not belongs to him/her -> IllegalArgumentException
	 * - A non registered user tries to accept an application -> IllegalArgumentException
	 * - An administrator tries to accept an application -> IllegalArgumentException
	 */

	@Test
	public void acceptApplicationDriver() {
		final Object testingData[][] = {
			{
				"customer2", 136, null
			}, {
				"customer3", 137, IllegalArgumentException.class
			}, {
				"customer1", 136, IllegalArgumentException.class
			}, {
				null, 136, IllegalArgumentException.class
			}, {
				"admin", 136, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptApplicationTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: A registered customer that owns an offer or a request denies an
	 * application of that offer or request
	 * Expected errors:
	 * - That user tries to deny some accepted or denied application -> IllegalArgumentException
	 * - That user tries to deny some application that not belongs to him/her -> IllegalArgumentException
	 * - A non registered user tries to deny an application -> IllegalArgumentException
	 * - An administrator tries to deny an application -> IllegalArgumentException
	 */

	@Test
	public void denyApplicationDriver() {
		final Object testingData[][] = {
			{
				"customer2", 136, null
			}, {
				"customer3", 137, IllegalArgumentException.class
			}, {
				"customer1", 136, IllegalArgumentException.class
			}, {
				null, 136, IllegalArgumentException.class
			}, {
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

	// Ancillary methods ------------------------------------------------------

	protected void acceptApplicationTemplate(final String username, final int applicationId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Application app;
			Collection<Application> apps;
			Customer principal;

			this.authenticate(username);

			app = this.applicationService.findOne(applicationId);
			principal = this.customerService.findByPrincipal();
			apps = this.applicationService.findApplicationsByCustomer(principal.getId());

			this.applicationService.accept(app);

			Assert.isTrue(apps.contains(app));

			this.unauthenticate();

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
			Collection<Application> apps;
			Customer principal;

			this.authenticate(username);

			app = this.applicationService.findOne(applicationId);
			principal = this.customerService.findByPrincipal();
			apps = this.applicationService.findApplicationsByCustomer(principal.getId());

			this.applicationService.deny(app);

			Assert.isTrue(apps.contains(app));

			this.unauthenticate();

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
			Trip trip;
			Collection<Application> apps;
			final Customer principal;
			int id;

			this.authenticate(username);

			app = this.applicationService.create();
			trip = this.tripService.findOne(tripId);
			principal = this.customerService.findByPrincipal();
			id = principal.getId();

			app.setTrip(trip);
			saved = this.applicationService.save(app);
			this.applicationService.flush();
			apps = this.applicationService.findApplicationsByCustomer(id);

			Assert.isTrue(apps.contains(saved));

			this.unauthenticate();

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
