
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import domain.Actor;
import domain.Application;
import domain.Customer;

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

	public Application save(final Application application) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));
		Assert.notNull(application);

		Application result;
		Actor actor;
		Customer principal;

		result = this.applicationRepository.save(application);
		actor = this.actorService.findByPrincipal();
		principal = this.customerService.findOne(actor.getId());

		result.setCustomer(principal);

		return result;

	}

	public Collection<Application> findAll() {
		Collection<Application> result;

		result = this.applicationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public void delete(final Application application) {
		Assert.isTrue(this.actorService.checkAuthority("ADMINISTRATOR"));
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		this.applicationRepository.delete(application);

	}

	public void flush() {
		this.applicationRepository.flush();
	}

	// Other business methods ----------------------------------------------------

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

}
