
package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BannerService;
import controllers.AbstractController;
import domain.Banner;

@Controller
@RequestMapping("/banner/administrator")
public class BannerAdministratorController extends AbstractController {

	@Autowired
	private BannerService	bannerService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editBanner() {
		final Banner currentBanner = this.bannerService.getBanner();

		return this.createEditModelAndView(currentBanner);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBanner(@Valid final Banner banner, final BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(banner);
		else
			try {
				this.bannerService.save(banner);
				result = new ModelAndView("redirect:/");
			} catch (final IllegalArgumentException oops) {
				result = this.createEditModelAndView(banner, "misc.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Banner banner) {
		ModelAndView result;

		result = this.createEditModelAndView(banner, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Banner banner, final String message) {
		ModelAndView result;

		result = new ModelAndView("banner/edit");
		result.addObject("banner", banner);
		result.addObject("message", message);

		return result;
	}

}
