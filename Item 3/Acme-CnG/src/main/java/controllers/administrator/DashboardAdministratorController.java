
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ApplicationService;
import services.CommentService;
import services.CustomerService;
import services.MessageService;
import services.OfferService;
import services.RequestService;
import services.TripService;
import controllers.AbstractController;
import domain.Actor;
import domain.Customer;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Supporting services -----------------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private TripService			tripService;

	@Autowired
	private OfferService		offerService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private MessageService		messageService;

	@Autowired
	private CustomerService		customerService;

	@Autowired
	private CommentService		commentService;


	// Constructors -----------------------------------------------------------

	public DashboardAdministratorController() {
		super();
	}

	// Dashboard -----------------------------------------------------------

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard() {

		ModelAndView result;
		final Double ratioOR;
		final Double avgOC;
		final Double avgRC;
		final Double avgAO;
		final Double avgAR;
		final Collection<Customer> customerMAA;
		final Collection<Customer> customerMAD;
		final Double avgACA;
		final Double avgACO;
		final Double avgACR;
		final Double avgACPA;
		final Double avgACPC;
		final Collection<Actor> actor10moreavgCPA;
		final Collection<Actor> actor10lessavgCPA;
		final Long maxMSA;
		final Long minMSA;
		final Double avgMSA;
		final Long maxMRA;
		final Long minMRA;
		final Double avgMRA;
		final Collection<Actor> actorsMMS;
		final Collection<Actor> actorsMMR;

		ratioOR = this.numbers(this.tripService.findRatioOfferVersusRequest());
		avgOC = this.numbers(this.offerService.findAvgOfferPerCostumer());
		avgRC = this.numbers(this.requestService.findAvgRequestPerCustomer());
		avgAO = this.numbers(this.applicationService.findAvgApplicationsPerOffer());
		avgAR = this.numbers(this.applicationService.findAvgApplicationsPerRequest());
		customerMAA = this.customer(this.customerService.findCustomerWhoHasMoreApplicationsAccepted());
		customerMAD = this.customer(this.customerService.findCustomerWhoHasMoreApplicationsDenied());
		avgACA = this.numbers(this.commentService.findAverageCommentPerActor());
		avgACO = this.numbers(this.commentService.findAverageCommentPerOffer());
		avgACR = this.numbers(this.commentService.findAverageCommentPerRequest());
		avgACPA = this.numbers(this.commentService.findAverageCommentPerAdministrator());
		avgACPC = this.numbers(this.commentService.findAverageCommentPerCustomer());
		actor10moreavgCPA = this.actors(this.actorService.findActorPostMore10PerCentAVGCommentsPerActor());
		actor10lessavgCPA = this.actors(this.actorService.findActorPostLess10PerCentAVGCommentsPerActor());
		maxMSA = this.messageService.findMaxNumSntMsgPerActor();
		minMSA = this.messageService.findMinNumSntMsgPerActor();
		avgMSA = this.numbers(this.messageService.findAvgNumSntMsgPerActor());
		maxMRA = this.messageService.findMaxNumRecMsgPerActor();
		minMRA = this.messageService.findMinNumRecMsgPerActor();
		avgMRA = this.numbers(this.messageService.findAvgNumRecMsgPerActor());
		actorsMMS = this.actor(this.actorService.findActorWithMoreSentMessages());
		actorsMMR = this.actor(this.actorService.findActorWithMoreReceivedMessages());

		result = new ModelAndView("administrator/dashboard");
		result.addObject("requestURI", "dashboard/administrator/dashboard.do");
		result.addObject("ratioOR", ratioOR);
		result.addObject("avgOC", avgOC);
		result.addObject("avgRC", avgRC);
		result.addObject("avgAO", avgAO);
		result.addObject("avgAR", avgAR);
		result.addObject("customerMAA", customerMAA);
		result.addObject("customerMAD", customerMAD);
		result.addObject("avgACA", avgACA);
		result.addObject("avgACO", avgACO);
		result.addObject("avgACR", avgACR);
		result.addObject("avgACPA", avgACPA);
		result.addObject("avgACPC", avgACPC);
		result.addObject("actor10moreavgCPA", actor10moreavgCPA);
		result.addObject("actor10lessavgCPA", actor10lessavgCPA);
		result.addObject("maxMSA", maxMSA);
		result.addObject("minMSA", minMSA);
		result.addObject("avgMSA", avgMSA);
		result.addObject("maxMRA", maxMRA);
		result.addObject("minMRA", minMRA);
		result.addObject("avgMRA", avgMRA);
		result.addObject("actorsMMS", actorsMMS);
		result.addObject("actorsMMR", actorsMMR);

		return result;

	}
	//Ancillary methods -----------------------------------

	public Collection<Customer> customer(final Customer customer) {
		Collection<Customer> result;

		result = new ArrayList<Customer>();

		if (customer != null)
			result.add(customer);

		return result;
	}

	public Collection<Actor> actor(final Actor actor) {
		Collection<Actor> result;

		result = new ArrayList<Actor>();

		if (actor != null)
			result.add(actor);

		return result;
	}

	public Collection<Actor> actors(final Collection<Actor> actors) {
		Collection<Actor> result;

		result = new ArrayList<Actor>();

		if (actors != null)
			result.addAll(actors);

		return result;
	}

	public Double numbers(final Double number) {
		Double result;

		result = 0.;

		if (number != null)
			result = number;

		return result;
	}

}
