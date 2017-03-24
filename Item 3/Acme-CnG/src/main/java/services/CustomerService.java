
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Customer;
import forms.CustomerForm;

@Service
@Transactional
public class CustomerService {

	// Managed repository ----------------------------------

	@Autowired
	private CustomerRepository	customerRepository;

	// Supporting services ---------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


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
		res.setUserAccount(this.userAccountService.create("LESSOR"));
		return res;
	}

	public Customer save(final Customer customer) {
		Assert.notNull(customer);

		Customer res;

		customer.getUserAccount().setPassword(this.hashCodePassword(customer.getUserAccount().getPassword()));

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

	public Customer reconstruct(final CustomerForm customerForm, final BindingResult binding) {
		Customer result;
		Authority auth;
		auth = new Authority();
		auth.setAuthority("CUSTOMER");

		result = customerForm.getCustomer();

		result.getUserAccount().addAuthority(auth);

		this.validator.validate(result, binding);

		return result;
	}

	// Other business methods ------------------------------

	public String hashCodePassword(final String password) {
		String result;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		result = encoder.encodePassword(password, null);

		return result;
	}

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

		res = this.customerRepository.findCustomerWhoHasMoreApplicationsAccepted().get(0);

		return res;
	}

	public Customer findCustomerWhoHasMoreApplicationsDenied() {
		Customer res;

		res = this.customerRepository.findCustomerWhoHasMoreApplicationsDenied().get(0);

		return res;
	}

}
