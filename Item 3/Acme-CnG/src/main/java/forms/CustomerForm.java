
package forms;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import security.UserAccount;
import domain.Customer;

public class CustomerForm {

	private Integer		id;
	private Integer		version;
	private String		name;
	private String		surname;
	private String		email;
	private String		phone;
	private UserAccount	userAccount;

	// Form
	private String		confirmPassword;


	public CustomerForm() {
		super();
		this.confirmPassword = "";
	}

	/**
	 * Crear objeto CustomerForm a partir de un Customer. Es necesario inicializar todas sus propiedades.
	 * 
	 * @param customer
	 *            Objeto CustomerForm para vista
	 */

	public CustomerForm(final Customer customer) {
		this();
		this.id = customer.getId();
		this.version = customer.getVersion();
		this.name = customer.getName();
		this.surname = customer.getSurname();
		this.email = customer.getEmail();
		this.phone = customer.getPhone();
		this.userAccount = customer.getUserAccount();
	}
	/**
	 * Reconstruir Customer a partir de CustomerForm
	 * 
	 * @return Customer Objeto Customer original
	 */
	public Customer getCustomer() {
		final Customer customer = new Customer();
		customer.setId(this.id);
		customer.setVersion(this.version);
		customer.setName(this.name);
		customer.setSurname(this.surname);
		customer.setEmail(this.email);
		customer.setPhone(this.phone);
		customer.setUserAccount(this.userAccount);

		return customer;
	}

	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(final Integer version) {
		this.version = version;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Pattern(regexp = "^\\+\\d{1,4}[\\s\\S]+$")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getConfirmPassword() {
		return this.confirmPassword;
	}

	public void setConfirmPassword(final String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
