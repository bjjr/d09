
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PlaceRepository;
import domain.Place;

@Service
@Transactional
public class PlaceService {

	// Managed repository

	@Autowired
	private PlaceRepository	placeRepository;


	// Simple CRUD methods

	public Place create() {
		final Place res = new Place();

		res.setAddress("");
		res.setCoordinates("");

		return res;
	}

	public Place save(final Place place) {
		Assert.notNull(place);
		Place res;

		res = this.placeRepository.save(place);
		return res;
	}

	public Place findOne(final int placeId) {
		Place res;

		res = this.placeRepository.findOne(placeId);
		Assert.notNull(res);

		return res;
	}

}
