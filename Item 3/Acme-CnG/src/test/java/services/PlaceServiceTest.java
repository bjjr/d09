
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Place;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PlaceServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private PlaceService	placeService;


	// Ancillary methods ------------------------------------------------------

	@Test
	public void testCreatePlace() {
		final Place place = this.placeService.create();
		this.authenticate("customer1");
		this.placeService.save(place);
		this.unauthenticate();
	}

}
