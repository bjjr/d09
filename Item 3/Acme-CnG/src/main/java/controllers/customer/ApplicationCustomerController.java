
package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CustomerService;
import controllers.AbstractController;
import domain.Application;
import domain.Customer;

@Controller
@RequestMapping("/application/customer")
public class ApplicationCustomerController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private CustomerService		customerService;


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
		result.addObject("requestURI", "application/customer/list.do");
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

		result = new ModelAndView("application/listByTrip");
		result.addObject("requestURI", "application/customer/listByTrip.do");
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

		application = this.applicationService.create();

		try {
			Assert.isTrue(!this.applicationService.checksIsMyTrip(tripId), "application.myTrip");
			Assert.isTrue(!this.applicationService.checkApplicationExists(tripId), "application.exists.error");
			this.applicationService.save(application, tripId);
			result = new ModelAndView("redirect:list.do");
			result.addObject("message", "application.commit.ok");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
			result.addObject("message", "application.commit.error");
		}

		return result;
	}
}
