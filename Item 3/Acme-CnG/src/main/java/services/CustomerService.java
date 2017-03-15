
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CustomerRepository;
import domain.Customer;

@Service
@Transactional
public class CustomerService {

	// Managed repository ----------------------------------

	@Autowired
	private CustomerRepository	customerRepository;

	// Supporting services ---------------------------------

	@Autowired
	private ActorService		actorService;


	// Constructor -----------------------------------------

	public CustomerService() {
		super();
	}

	// Simple CRUD methods ---------------------------------

	public Customer create() {
		final Customer res = new Customer();

		res.setName("");
		res.setSurname("");
		res.setPhone("");
		res.setEmail("");

		return res;
	}

	public Customer save(final Customer customer) {
		Assert.notNull(customer);

		Customer res;

		res = this.customerRepository.save(customer);

		return res;
	}

	public Customer findOne(final int customerId) {
		Assert.isTrue(customerId != 0);

		Customer res;

		res = this.customerRepository.findOne(customerId);
		Assert.notNull(res, "The customer was not found");

		return res;
	}

	// Other business methods ------------------------------

}
