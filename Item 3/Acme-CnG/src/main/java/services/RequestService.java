
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RequestRepository;
import domain.Application;
import domain.Customer;
import domain.Request;
import domain.Trip;

@Service
@Transactional
public class RequestService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;

	// Supporting services -----------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private TripService			tripService;

	@Autowired
	private CustomerService		customerService;

	// Validator --------------------------------------------

	@Autowired
	private Validator			validator;


	// Constructors -----------------------------------------------------

	public RequestService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Request create() {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Request result;
		Boolean banned;

		result = new Request();
		banned = false;

		result.setBanned(banned);

		return result;
	}

	public Request findOne(final int requestId) {
		Request result;

		result = this.requestRepository.findOne(requestId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Request> findAll() {
		Collection<Request> result;

		result = this.requestRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Request save(final Request request) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));

		Assert.notNull(request);

		Request result;
		Customer customer;
		Collection<Request> requestsByCustomer;

		customer = this.customerService.findByPrincipal();

		request.setCustomer(customer);
		request.setBanned(false);

		requestsByCustomer = this.findRequestsByCustomer(customer.getId());
		requestsByCustomer.add(request);
		this.customerService.save(customer);

		result = this.requestRepository.save(request);

		return result;
	}

	public void flush() {
		this.requestRepository.flush();
	}

	// Other business methods ----------------------------------------------------

	public void accept(final Application application) {
		this.tripService.accept(application);
	}

	public void deny(final Application application) {
		this.tripService.deny(application);
	}

	public void ban(final Request request) {
		this.tripService.ban(request);
	}

	public Collection<Request> findByKeyword(final String keyword) {
		final Collection<Request> result;
		Collection<Trip> trips;
		Customer customer;

		result = new ArrayList<Request>();
		trips = this.tripService.findByKeyword(keyword);
		customer = this.customerService.findByPrincipal();

		for (final Trip t : trips)
			if (t instanceof Request)
				if (!t.getCustomer().equals(customer))
					result.add((Request) t);

		return result;

	}

	public Collection<Request> findAllNotBanned() {
		Collection<Request> result;
		Collection<Trip> trips;
		Customer customer;

		result = new ArrayList<Request>();
		trips = this.tripService.findAllNotBanned();
		customer = this.customerService.findByPrincipal();

		for (final Trip t : trips)
			if (t instanceof Request)
				if (!t.getCustomer().equals(customer))
					result.add((Request) t);

		return result;
	}

	public Collection<Request> findRequestsByCustomer(final int customerId) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		final Collection<Request> result;
		Collection<Trip> trips;

		trips = this.tripService.findTripsByCustomer(customerId);
		result = new ArrayList<Request>();

		for (final Trip t : trips)
			if (t instanceof Request)
				result.add((Request) t);

		return result;
	}

	public Double findAvgRequestPerCustomer() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.requestRepository.findAvgRequestPerCustomer();
		Assert.notNull(result);

		return result;
	}

	public Request reconstruct(final Request request, final BindingResult binding) {
		Request result;
		Customer customer;

		if (request.getId() == 0) {
			customer = this.customerService.findByPrincipal();
			result = request;
			result.setCustomer(customer);
		} else {
			Request aux;
			aux = this.findOne(request.getId());
			result = request;
			result.setCustomer(aux.getCustomer());
		}

		this.validator.validate(result, binding);

		return result;
	}

}
