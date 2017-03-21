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
	 * Use case: A registered user creates an application
	 * Expected errors:
	 * - A non registered user tries to create an application -> IllegalArgumentException
	 */

	@Test
	public void createApplicationDriver() {
		final Object testingData[][] = {
			{
				"customer2", 115, null
			}, {
				null, 115, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createApplicationTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: A registered user lists his/her applications
	 * Expected errors:
	 * - A non registered user tries to list applications -> IllegalArgumentException
	 */

	@Test
	public void listApplicationDriver() {
		final Object testingData[][] = {
			{
				"customer2", null
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listApplicationTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/*
	 * Use case: A registered user that owns an offer or a request accepts an
	 * application of that offer or request
	 * Expected errors:
	 * - That user tries to accept some accepted or denied application -> IllegalArgumentException
	 * - That user tries to accept some application that not belongs to him/her -> IllegalArgumentException
	 * - A non registered user tries to accept an application -> IllegalArgumentException
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
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptApplicationTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/*
	 * Use case: A registered user that owns an offer or a request denies an
	 * application of that offer or request
	 * Expected errors:
	 * - That user tries to deny some accepted or denied application -> IllegalArgumentException
	 * - That user tries to deny some application that not belongs to him/her -> IllegalArgumentException
	 * - A non registered user tries to deny an application -> IllegalArgumentException
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
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.denyApplicationTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
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

			this.authenticate(username);

			app = this.applicationService.findOne(applicationId);

			this.applicationService.deny(app);

			this.unauthenticate();

		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void listApplicationTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			Collection<Application> apps;
			final Customer principal;
			int id;

			this.authenticate(username);

			principal = this.customerService.findByPrincipal();
			id = principal.getId();

			apps = this.applicationService.findApplicationsByCustomer(id);

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
