
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TripRepository;
import domain.Application;
import domain.Trip;

@Service
@Transactional
public class TripService {

	// Managed repository -----------------------------------

	@Autowired
	private TripRepository		tripRepository;

	// Supporting services ----------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ApplicationService	applicationService;


	// Constructors -----------------------------------------

	public TripService() {
		super();
	}

	// Simple CRUD methods ----------------------------------

	public Collection<Trip> findAll() {
		Collection<Trip> result;

		result = this.tripRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Trip findOne(final int tripId) {
		Assert.notNull(tripId);
		Assert.isTrue(tripId != 0);

		Trip result;

		result = this.tripRepository.findOne(tripId);
		Assert.notNull(result);

		return result;
	}

	// Other business methods -------------------------------

	public Trip ban(final Trip trip) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Assert.notNull(trip);

		Trip result;
		Collection<Application> applications;

		applications = this.applicationService.findApplicationsByTrip(trip.getId());

		result = trip;
		result.setBanned(true);

		for (final Application ap : applications)
			this.applicationService.delete(ap);

		return result;
	}

	public Collection<Trip> findByKeyword(final String keyword) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Collection<Trip> result;

		result = this.tripRepository.findByKeyword(keyword);
		result.retainAll(this.findAllNotBanned());

		Assert.notNull(result);

		return result;

	}

	public Collection<Trip> findAllNotBanned() {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Collection<Trip> result;

		result = this.tripRepository.findAllNotBanned();
		Assert.notNull(result);

		return result;
	}

	public Collection<Trip> findTripsByCustomer(final int customerId) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Collection<Trip> result;

		result = this.tripRepository.findTripsByCustomer(customerId);
		Assert.notNull(result);

		return result;
	}

	public Double findRatioOfferVersusRequest() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.tripRepository.findRatioOfferVersusRequest();

		return result;
	}

}
