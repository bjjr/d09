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
import domain.Banner;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BannerServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private BannerService	bannerService;


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
				"admin", "http://www.imagen.com.mx/assets/img/imagen_share.png", null
			}, {
				null, "http://www.imagen.com.mx/assets/img/imagen_share.png", IllegalArgumentException.class
			}, {
				"customer1", "http://www.imagen.com.mx/assets/img/imagen_share.png", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.saveBannerTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void saveBannerTemplate(final String username, final String url, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			final Banner banner = this.bannerService.getBanner();
			banner.setPath(url);

			final Banner savedBanner = this.bannerService.save(banner);

			Assert.isTrue(url.equals(savedBanner.getPath()), "The saved URL doesnt correspond with the proposed URL change");

			this.unauthenticate();
		} catch (final Throwable th) {
			caught = th.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
