
package controllers;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.BannerService;
import utilities.AbstractTest;
import controllers.administrator.BannerAdministratorController;
import domain.Banner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BannerControllerTest extends AbstractTest {

	@InjectMocks
	BannerAdministratorController	bannerController;

	@Mock
	private BannerService			bannerService;

	private MockMvc					mockMvc;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.bannerController).build();
	}

	@Test
	public void addEmptyBanner() throws Exception {
		this.authenticate("admin");

		Mockito.when(this.bannerService.save(Matchers.any(Banner.class))).thenThrow(new RuntimeException("For Testing"));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/banner/administrator/edit").param("URL", "http://sample.com")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.forwardedUrl("/banner/administrator/edit"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("page_error"));
	}

}
