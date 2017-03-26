
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.RequestService;
import domain.Actor;
import domain.Administrator;
import domain.Customer;
import domain.Request;

@Controller
@RequestMapping("/request")
public class RequestController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private RequestService	requestService;

	@Autowired
	private ActorService	actorService;


	// Constructors -------------------------------------------

	public RequestController() {
		super();
	}

	// Listing ------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Request> requests;
		final Actor actor;
		final Boolean myRequests;
		Collection<Request> myRequestsWithApplicationsMine;

		actor = this.actorService.findByPrincipal();
		requests = new ArrayList<Request>();
		myRequests = false;
		myRequestsWithApplicationsMine = new ArrayList<Request>();

		if (actor instanceof Administrator)
			requests = this.requestService.findAll();
		else if (actor instanceof Customer) {
			myRequestsWithApplicationsMine = this.requestService.findRequestsWithApplicationsMine();
			requests = this.requestService.findAllNotBanned();
		}

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("myRequests", myRequests);
		result.addObject("myRequestsWithApplicationsMine", myRequestsWithApplicationsMine);
		result.addObject("requestURI", "request/list.do");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, params = "search")
	public ModelAndView search(@RequestParam final String keyword) {
		ModelAndView result;
		Collection<Request> requests;
		Boolean myRequests;
		final Collection<Request> myRequestsWithApplicationsMine;

		requests = this.requestService.findByKeyword(keyword);
		myRequests = false;
		myRequestsWithApplicationsMine = this.requestService.findRequestsWithApplicationsMine();

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);
		result.addObject("myRequests", myRequests);
		result.addObject("myRequestsWithApplicationsMine", myRequestsWithApplicationsMine);
		result.addObject("keyword", keyword);
		result.addObject("requestURI", "request/list.do");

		return result;
	}
}
