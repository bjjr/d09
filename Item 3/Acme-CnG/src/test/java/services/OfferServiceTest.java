
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;

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

	protected void findByKeywordTemplate(final String username, final String keyword, final Class<?> expected) {
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

	@Test
	public void findByKeywordDriver() {
		final Object testingData[][] = {
			{
				null, "test", IllegalArgumentException.class
			}, {
				"admin", "test", IllegalArgumentException.class
			}, {
				"customer1", "", null
			}, {
				"customer2", null, null
			}, {
				"customer1", "C/Place1Address", null
			}, {
				"customer2", "Destination", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.findByKeywordTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
}
