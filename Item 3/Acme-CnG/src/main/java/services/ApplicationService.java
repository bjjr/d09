
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import domain.Application;
import domain.Customer;
import domain.Trip;

@Service
@Transactional
public class ApplicationService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ApplicationRepository	applicationRepository;

	// Supporting services -----------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CustomerService			customerService;

	@Autowired
	private TripService				tripService;


	// Constructors -----------------------------------------------------

	public ApplicationService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Application create() {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Application result;

		result = new Application();
		result.setStatus("PENDING");

		return result;
	}

	public Application findOne(final int applicationId) {
		Application result;

		result = this.applicationRepository.findOne(applicationId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Application> findAll() {
		Collection<Application> result;

		result = this.applicationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Application save(final Application application) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Assert.notNull(application);

		Application result;
		Customer principal;

		principal = this.customerService.findByPrincipal();
		application.setCustomer(principal);
		result = this.applicationRepository.save(application);

		return result;

	}

	public void delete(final Application application) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		this.applicationRepository.delete(application);

	}

	public void flush() {
		this.applicationRepository.flush();
	}

	// Other business methods ----------------------------------------------------

	public void accept(final Application application) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Assert.notNull(application);
		Assert.isTrue(application.getStatus().equals("PENDING"));

		application.setStatus("ACCEPTED");
	}

	public void deny(final Application application) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Assert.notNull(application);
		Assert.isTrue(application.getStatus().equals("PENDING"));

		application.setStatus("DENIED");
	}

	public Collection<Application> findApplicationsByCustomer(final int customerId) {
		Assert.notNull(customerId);
		Assert.isTrue(customerId != 0);

		Collection<Application> result;

		result = this.applicationRepository.findApplicationsByCustomer(customerId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Application> findApplicationsByTrip(final int tripId) {
		Assert.notNull(tripId);
		Assert.isTrue(tripId != 0);

		Collection<Application> result;

		result = this.applicationRepository.findApplicationsByTrip(tripId);
		Assert.notNull(result);

		return result;
	}

	public Double findAvgApplicationsPerOffer() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.applicationRepository.findAvgApplicationsPerOffer();
		Assert.notNull(result);

		return result;
	}

	public Double findAvgApplicationsPerRequest() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.applicationRepository.findAvgApplicationsPerRequest();
		Assert.notNull(result);

		return result;
	}

	public Collection<Application> findApplicationsAllTripsOfCustomer(final Customer customer) {
		Assert.notNull(customer);

		final Collection<Application> result;
		Collection<Trip> trips;

		trips = this.tripService.findTripsByCustomer(customer.getId());
		result = new ArrayList<Application>();

		for (final Trip t : trips)
			result.addAll(this.findApplicationsByTrip(t.getId()));

		return result;
	}

	public Boolean checkApplicationExists(final int tripId) {
		Boolean result;
		Customer customer;
		Collection<Application> myApplications;
		Trip trip;

		result = false;
		trip = this.tripService.findOne(tripId);
		customer = this.customerService.findByPrincipal();
		myApplications = this.findApplicationsByCustomer(customer.getId());

		for (final Application app : myApplications)
			if (app.getTrip().equals(trip)) {
				result = true;
				break;
			}
		return result;
	}

}
