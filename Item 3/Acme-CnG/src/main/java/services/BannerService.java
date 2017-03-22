
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BannerRepository;
import domain.Banner;

@Service
@Transactional
public class BannerService {

	@Autowired
	private BannerRepository	bannerRepository;

	@Autowired
	private ActorService		actorService;


	public BannerService() {
		super();
	}

	public Banner getBanner() {
		final Banner banner = this.bannerRepository.getBanner();
		Assert.notNull(banner);

		return banner;
	}

	public Banner save(final Banner banner) {

		Assert.isTrue(this.actorService.checkAuthority("ADMIN"), "BannerService.save: You need to be admin in order to save a banner");

		final Banner res = this.bannerRepository.save(banner);
		Assert.notNull(res);

		return res;
	}
}
