
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomerService;
import domain.Customer;
import forms.CustomerForm;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

	@Autowired
	private CustomerService	customerService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Customer customer = this.customerService.create();

		result = this.createEditModelAndView(new CustomerForm(customer));

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final CustomerForm customerForm, final BindingResult binding) {
		ModelAndView result;
		Customer customer;

		customer = this.customerService.reconstruct(customerForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(customerForm);
		else if (!customer.getUserAccount().getPassword().equals(customerForm.getConfirmPassword()))
			result = this.createEditModelAndView(customerForm, "customer.commit.password");
		else
			try {
				this.customerService.save(customer);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final IllegalArgumentException oops) {
				result = this.createEditModelAndView(customerForm, "customer.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndView(final CustomerForm customer) {
		ModelAndView result;

		result = this.createEditModelAndView(customer, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final CustomerForm customer, final String message) {
		ModelAndView result;

		result = new ModelAndView("customer/create");
		result.addObject("customerForm", customer);
		result.addObject("message", message);

		return result;
	}

}
