
package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CustomerService;
import services.TripService;
import controllers.AbstractController;
import domain.Application;
import domain.Customer;
import domain.Trip;

@Controller
@RequestMapping("/application/customer")
public class ApplicationCustomerController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private TripService			tripService;


	// Constructors -------------------------------------------

	public ApplicationCustomerController() {
		super();
	}

	// Listings -------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Application> applications;
		Customer principal;

		principal = this.customerService.findByPrincipal();
		applications = this.applicationService.findApplicationsByCustomer(principal.getId());

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);

		return result;
	}

	@RequestMapping(value = "/listByTrip", method = RequestMethod.GET)
	public ModelAndView listByTrip() {
		ModelAndView result;
		final Collection<Application> applications;
		Customer principal;

		principal = this.customerService.findByPrincipal();
		applications = this.applicationService.findApplicationsAllTripsOfCustomer(principal);

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);

		return result;
	}

	// Accept and deny -------------------------------------------

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		application = this.applicationService.findOne(applicationId);

		try {
			this.applicationService.accept(application);
			result = new ModelAndView("redirect:listByTrip.do");
			result.addObject("message", "application.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:listByTrip.do");
			result.addObject("message", "application.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/deny", method = RequestMethod.GET)
	public ModelAndView deny(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		application = this.applicationService.findOne(applicationId);

		try {
			this.applicationService.deny(application);
			result = new ModelAndView("redirect:listByTrip.do");
			result.addObject("message", "application.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:listByTrip.do");
			result.addObject("message", "application.commit.error");
		}

		return result;
	}

	// Create -------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		ModelAndView result;
		Application application;
		final Trip trip;

		application = this.applicationService.create();
		trip = this.tripService.findOne(tripId);

		try {
			application.setTrip(trip);
			this.applicationService.save(application);
			result = new ModelAndView("redirect:list.do");
			result.addObject("message", "application.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("message", "application.commit.error");
		}

		return result;
	}
}
