
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.OfferService;
import controllers.AbstractController;
import domain.Offer;

@Controller
@RequestMapping("/offer/admin")
public class OfferAdminController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private OfferService	offerService;


	// Constructors -------------------------------------------

	public OfferAdminController() {
		super();
	}

	// Ban ----------------------------------------------------

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int offerId) {
		ModelAndView result;
		final Offer offer;

		offer = this.offerService.findOne(offerId);

		this.offerService.ban(offer);
		this.offerService.save(offer);
		result = new ModelAndView("redirect:/offer/list.do");

		return result;
	}

}
