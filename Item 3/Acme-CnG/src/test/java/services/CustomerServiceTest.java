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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Customer;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CustomerServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private CustomerService	customerService;


	// Tests ------------------------------------------------------------------

	/*
	 * Checks if a non registered user can register.
	 * Checks if a registered user cannot register.
	 */

	@Test
	public void registrationDriver() {
		final Object testingData[][] = {
			{
				null, null
			}, {
				"customer1", IllegalArgumentException.class
			}, {
				"admin", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registrationTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/*
	 * Checks if the returned customer is who has more applications accepted
	 */

	@Test
	public void testFindCustomerWhoHasMoreApplicationsAccepted() {
		Customer customer;

		customer = this.customerService.findCustomerWhoHasMoreApplicationsAccepted();

		Assert.isTrue(customer.equals(this.customerService.findOne(101)));
	}

	/*
	 * Checks if the returned customer is who has more applications denied
	 */

	@Test
	public void testFindCustomerWhoHasMoreApplicationsDenied() {
		Customer customer;

		customer = this.customerService.findCustomerWhoHasMoreApplicationsDenied();

		Assert.isTrue(customer.equals(this.customerService.findOne(101)));
	}

	// Ancillary methods ------------------------------------------------------

	protected void registrationTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			this.customerService.create();
			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
