
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
import forms.OfferForm;

@Service
@Transactional
public class OfferService {

	// Managed repository -----------------------------------

	@Autowired
	private OfferRepository		offerRepository;

	// Supporting services ----------------------------------

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private TripService			tripService;

	@Autowired
	private ApplicationService	applicationService;

	// Validator --------------------------------------------

	@Autowired
	private Validator			validator;


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

	public void ban(final Offer offer) {
		this.tripService.ban(offer);
	}

	public Collection<Offer> findByKeyword(final String keyword) {
		Collection<Offer> result;
		Collection<Trip> trips;
		Customer customer;

		result = new ArrayList<Offer>();
		trips = this.tripService.findByKeyword(keyword);
		customer = this.customerService.findByPrincipal();

		if (keyword == "" || keyword.equals(null))
			result = this.findAllNotBanned();
		else
			for (final Trip t : trips)
				if (t instanceof Offer)
					if (!t.getCustomer().equals(customer))
						result.add((Offer) t);

		return result;
	}
	public Collection<Offer> findAllNotBanned() {
		Collection<Offer> result;
		Collection<Trip> trips;
		Customer customer;

		result = new ArrayList<Offer>();
		trips = this.tripService.findAllNotBanned();
		customer = this.customerService.findByPrincipal();

		for (final Trip t : trips)
			if (t instanceof Offer)
				if (!t.getCustomer().equals(customer))
					result.add((Offer) t);

		return result;
	}

	public Collection<Offer> findOffersByCustomer(final int customerId) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		final Collection<Offer> result;
		Collection<Trip> trips;

		trips = this.tripService.findTripsByCustomer(customerId);
		result = new ArrayList<Offer>();

		for (final Trip t : trips)
			if (t instanceof Offer)
				result.add((Offer) t);

		return result;
	}

	public Collection<Offer> findOffersWithApplicationsMine() {
		final Collection<Offer> result;
		Collection<Application> allMyApplications;
		Customer customer;

		result = new ArrayList<Offer>();
		customer = this.customerService.findByPrincipal();
		allMyApplications = this.applicationService.findApplicationsByCustomer(customer.getId());

		for (final Application a : allMyApplications)
			if (a.getTrip() instanceof Offer)
				result.add((Offer) a.getTrip());

		return result;
	}

	public Double findAvgOfferPerCostumer() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.offerRepository.findAvgOfferPerCostumer();
		Assert.notNull(result);

		return result;
	}

	public Offer reconstruct(final OfferForm offerForm, final BindingResult binding) {
		Offer result;
		Customer customer;

		result = offerForm.getOffer();
		customer = this.customerService.findByPrincipal();
		result.setCustomer(customer);

		this.validator.validate(result, binding);

		return result;
	}

}
