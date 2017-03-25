
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
import services.OfferService;
import domain.Actor;
import domain.Administrator;
import domain.Customer;
import domain.Offer;

@Controller
@RequestMapping("/offer")
public class OfferController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private OfferService	offerService;

	@Autowired
	private ActorService	actorService;


	// Constructors -------------------------------------------

	public OfferController() {
		super();
	}

	// Listing ------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Offer> offers;
		final Actor actor;
		final Boolean myOffers;
		Collection<Offer> myOffersWithApplicationsMine;

		actor = this.actorService.findByPrincipal();
		offers = new ArrayList<Offer>();
		myOffers = false;
		myOffersWithApplicationsMine = new ArrayList<Offer>();

		if (actor instanceof Administrator)
			offers = this.offerService.findAll();
		else if (actor instanceof Customer) {
			myOffersWithApplicationsMine = this.offerService.findOffersWithApplicationsMine();
			offers = this.offerService.findAllNotBanned();
		}

		result = new ModelAndView("offer/list");
		result.addObject("offers", offers);
		result.addObject("myOffers", myOffers);
		result.addObject("myOffersWithApplicationsMine", myOffersWithApplicationsMine);
		result.addObject("requestURI", "offer/list.do");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, params = "search")
	public ModelAndView search(@RequestParam final String keyword) {
		ModelAndView result;
		Collection<Offer> offers;
		Boolean myOffers;
		final Collection<Offer> myOffersWithApplicationsMine;

		offers = this.offerService.findByKeyword(keyword);
		myOffers = false;
		myOffersWithApplicationsMine = this.offerService.findOffersWithApplicationsMine();

		result = new ModelAndView("offer/list");
		result.addObject("offers", offers);
		result.addObject("myOffers", myOffers);
		result.addObject("myOffersWithApplicationsMine", myOffersWithApplicationsMine);
		result.addObject("keyword", keyword);
		result.addObject("requestURI", "offer/list.do");

		return result;
	}
}
