
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
import services.OfferService;
import controllers.AbstractController;
import domain.Customer;
import domain.Offer;
import forms.OfferForm;

@Controller
@RequestMapping("/offer/customer")
public class OfferCustomerController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private OfferService	offerService;

	@Autowired
	private CustomerService	customerService;


	// Constructors -------------------------------------------

	public OfferCustomerController() {
		super();
	}

	// Creating -----------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Offer offer;
		OfferForm offerForm;

		offer = this.offerService.create();
		offerForm = new OfferForm(offer);
		result = this.createEditModelAndView(offerForm);

		return result;
	}

	// Listing ------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Offer> offers;
		Customer customer;
		Boolean myOffers;

		customer = this.customerService.findByPrincipal();
		offers = this.offerService.findOffersByCustomer(customer.getId());
		myOffers = true;

		result = new ModelAndView("offer/list");
		result.addObject("offers", offers);
		result.addObject("myOffers", myOffers);
		result.addObject("requestURI", "offer/list.do");

		return result;
	}

	// Saving ------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final OfferForm offerForm, final BindingResult binding) {
		ModelAndView result;
		Offer offer;

		offer = this.offerService.reconstruct(offerForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(offerForm);
		else
			try {
				this.offerService.save(offer);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(offerForm, "misc.commit.error");
			}

		return result;
	}
	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final OfferForm offerForm) {
		ModelAndView result;

		result = this.createEditModelAndView(offerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final OfferForm offerForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("offer/create");
		result.addObject("offerForm", offerForm);
		result.addObject("message", message);

		return result;
	}
}
