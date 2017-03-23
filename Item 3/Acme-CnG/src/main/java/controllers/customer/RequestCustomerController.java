
package controllers.customer;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomerService;
import services.RequestService;
import controllers.AbstractController;
import domain.Customer;
import domain.Request;
import forms.RequestForm;

@Controller
@RequestMapping("/request/customer")
public class RequestCustomerController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private RequestService	requestService;

	@Autowired
	private CustomerService	customerService;


	// Constructors -------------------------------------------

	public RequestCustomerController() {
		super();
	}

	// Creating -----------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Request request;
		RequestForm requestForm;

		request = this.requestService.create();
		requestForm = new RequestForm(request);
		result = this.createEditModelAndView(requestForm);

		return result;
	}

	// Listing ------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Request> requests;
		Customer customer;
		Boolean myRequests;

		customer = this.customerService.findByPrincipal();
		requests = this.requestService.findRequestsByCustomer(customer.getId());
		myRequests = true;

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("myRequests", myRequests);
		result.addObject("requestURI", "request/list.do");

		return result;
	}

	// Saving ------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RequestForm requestForm, final BindingResult binding) {
		ModelAndView result;
		Request request;

		request = this.requestService.reconstruct(requestForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(requestForm);
		else
			try {
				this.requestService.save(request);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(requestForm, "misc.commit.error");
			}

		return result;
	}
	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final RequestForm requestForm) {
		ModelAndView result;

		result = this.createEditModelAndView(requestForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RequestForm requestForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("request/create");
		result.addObject("requestForm", requestForm);
		result.addObject("message", message);

		return result;
	}
}
