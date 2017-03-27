
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TripServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private TripService	tripService;


	// Tests ------------------------------------------------------------------

	/*
	 * Checks if the query findRatioOfferVersusRequest works
	 */

	@Test
	public void testFindRatioOfferVersusRequest() {
		this.authenticate("admin");

		Double avg;

		avg = this.tripService.findRatioOfferVersusRequest();
		Assert.isTrue(avg.equals(1.33333));

		this.unauthenticate();

	}

}
