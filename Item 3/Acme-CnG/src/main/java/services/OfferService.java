
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.OfferRepository;
import domain.Application;
import domain.Customer;
import domain.Offer;
import domain.Trip;

@Service
@Transactional
public class OfferService {

	// Managed repository -----------------------------------

	@Autowired
	private OfferRepository	offerRepository;

	// Supporting services ----------------------------------

	@Autowired
	private CustomerService	customerService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private TripService		tripService;

	// Validator --------------------------------------------

	@Autowired
	private Validator		validator;


	// Constructors -----------------------------------------

	public OfferService() {
		super();
	}

	// Simple CRUD methods ----------------------------------

	public Offer create() {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Offer result;
		Boolean banned;

		result = new Offer();
		banned = false;
		result.setBanned(banned);

		return result;
	}

	public Offer findOne(final int offerId) {
		Offer result;

		result = this.offerRepository.findOne(offerId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Offer> findAll() {
		Collection<Offer> result;

		result = this.offerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Offer save(final Offer offer) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));

		Assert.notNull(offer);

		Offer result;
		Customer customer;
		Collection<Offer> offersByCustomer;

		customer = this.customerService.findByPrincipal();

		offer.setCustomer(customer);
		offer.setBanned(false);

		offersByCustomer = this.findOffersByCustomer(customer.getId());
		offersByCustomer.add(offer);
		this.customerService.save(customer);

		result = this.offerRepository.save(offer);

		return result;
	}

	public void flush() {
		this.offerRepository.flush();
	}

	// Other business methods -------------------------------

	public void accept(final Application application) {
		this.tripService.accept(application);
	}

	public void deny(final Application application) {
		this.tripService.deny(application);
	}

	public void ban(final Offer offer) {
		this.tripService.ban(offer);
	}

	public Collection<Offer> findByKeyword(final String keyword) {
		final Collection<Offer> result;
		Collection<Trip> trips;

		result = new ArrayList<Offer>();
		trips = this.tripService.findByKeyword(keyword);

		for (final Trip t : trips)
			if (t instanceof Offer)
				result.add((Offer) t);

		return result;
	}

	public Collection<Offer> findAllNotBanned() {
		Collection<Offer> result;
		Collection<Trip> trips;

		result = new ArrayList<Offer>();
		trips = this.tripService.findAllNotBanned();

		for (final Trip t : trips)
			if (t instanceof Offer)
				result.add((Offer) t);

		return result;
	}

	public Collection<Offer> findOffersByCustomer(final int customerId) {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN") || this.actorService.checkAuthority("CUSTOMER"));

		final Collection<Offer> result;
		Collection<Trip> trips;

		trips = this.tripService.findTripsByCustomer(customerId);

		result = new ArrayList<Offer>();
		trips = this.tripService.findAllNotBanned();

		for (final Trip t : trips)
			if (t instanceof Offer)
				result.add((Offer) t);

		return result;
	}

	public Double findAvgOfferPerCostumer() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.offerRepository.findAvgOfferPerCostumer();
		Assert.notNull(result);

		return result;
	}

	public Offer reconstruct(final Offer offer, final BindingResult binding) {
		Offer result;
		Customer customer;

		if (offer.getId() == 0) {
			customer = this.customerService.findByPrincipal();
			result = offer;
			result.setCustomer(customer);
		} else {
			Offer aux;
			aux = this.findOne(offer.getId());
			result = offer;
			result.setCustomer(aux.getCustomer());
		}

		this.validator.validate(result, binding);

		return result;
	}

}
