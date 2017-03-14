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

import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ActorService	actorService;


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
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.RegistrationTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void RegistrationTemplate(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			this.actorService.create();
			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
