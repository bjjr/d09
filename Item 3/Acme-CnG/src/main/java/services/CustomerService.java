
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CustomerRepository;
import security.LoginService;
import security.UserAccount;
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
		Customer res;
		Assert.isTrue(!this.actorService.checkAuthority("ADMIN") && !this.actorService.checkAuthority("CUSTOMER"));

		res = new Customer();

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

	public Customer findByPrincipal() {
		Customer result = null;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);

		return result;
	}

	public Customer findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Customer res;

		res = this.customerRepository.findByUserAccountId(userAccount.getId());

		Assert.notNull(res);

		return res;
	}

	public Customer findCustomerWhoHasMoreApplicationsAccepted() {
		Customer res;

		res = null;

		if (!this.customerRepository.findCustomerWhoHasMoreApplicationsAccepted().isEmpty())
			res = this.customerRepository.findCustomerWhoHasMoreApplicationsAccepted().get(0);

		return res;
	}

	public Customer findCustomerWhoHasMoreApplicationsDenied() {
		Customer res;

		res = null;

		if (!this.customerRepository.findCustomerWhoHasMoreApplicationsDenied().isEmpty())
			res = this.customerRepository.findCustomerWhoHasMoreApplicationsDenied().get(0);

		return res;
	}

}
