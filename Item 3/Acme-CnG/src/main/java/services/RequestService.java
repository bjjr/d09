
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
import forms.RequestForm;

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

	@Autowired
	private ApplicationService	applicationService;

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
		Date thisMoment;

		customer = this.customerService.findByPrincipal();
		thisMoment = new Date(System.currentTimeMillis());

		request.setCustomer(customer);
		request.setBanned(false);
		Assert.isTrue(thisMoment.compareTo(request.getMoment()) < 0, "The moment must be in the future");

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

	public void ban(final Request request) {
		this.tripService.ban(request);
	}

	public Collection<Request> findByKeyword(final String keyword) {
		Collection<Request> result;
		Collection<Trip> trips;
		Customer customer;

		result = new ArrayList<Request>();
		trips = this.tripService.findByKeyword(keyword);
		customer = this.customerService.findByPrincipal();

		if (keyword == "" || keyword.equals(null))
			result = this.findAllNotBanned();
		else
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

	public Collection<Request> findRequestsWithApplicationsMine() {
		final Collection<Request> result;
		Collection<Application> allMyApplications;
		Customer customer;

		result = new ArrayList<Request>();
		customer = this.customerService.findByPrincipal();
		allMyApplications = this.applicationService.findApplicationsByCustomer(customer.getId());

		for (final Application a : allMyApplications)
			if (a.getTrip() instanceof Request)
				result.add((Request) a.getTrip());

		return result;
	}

	public Double findAvgRequestPerCustomer() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.requestRepository.findAvgRequestPerCustomer();
		Assert.notNull(result);

		return result;
	}

	public Request reconstruct(final RequestForm requestForm, final BindingResult binding) {
		Request result;
		Customer customer;

		result = requestForm.getRequest();
		customer = this.customerService.findByPrincipal();
		result.setCustomer(customer);

		this.validator.validate(result, binding);

		return result;
	}

}
