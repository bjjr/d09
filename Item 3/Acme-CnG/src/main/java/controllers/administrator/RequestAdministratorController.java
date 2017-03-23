
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RequestService;
import controllers.AbstractController;
import domain.Request;

@Controller
@RequestMapping("/request/administrator")
public class RequestAdministratorController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private RequestService	requestService;


	// Constructors -------------------------------------------

	public RequestAdministratorController() {
		super();
	}

	// Ban ----------------------------------------------------

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int requestId) {
		ModelAndView result;
		final Request request;

		request = this.requestService.findOne(requestId);

		this.requestService.ban(request);
		result = new ModelAndView("redirect:/request/list.do");

		return result;
	}

}
