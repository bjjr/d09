
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

import services.CustomerService;
import utilities.AbstractTest;
import domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CustomerControllerTest extends AbstractTest {

	private MockMvc			mockMvc;

	@InjectMocks
	CustomerController		customerController;

	@Mock
	private CustomerService	customerService;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.customerController).build();
	}

	@Test
	public void testCreateSignupFormInvalidUser() throws Exception {
		Mockito.when(this.customerService.save(Matchers.any(Customer.class))).thenThrow(new RuntimeException("For Testing"));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/security/login").param("username", "customer1").param("password", "customer1")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.forwardedUrl("index"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("page_error"));

	}
}
