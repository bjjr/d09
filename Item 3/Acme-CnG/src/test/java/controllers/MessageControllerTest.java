
package controllers;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import utilities.AbstractTest;
import converters.ActorToStringConverter;
import converters.StringToActorConverter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@WebAppConfiguration
public class MessageControllerTest extends AbstractTest {

	// Mockito MVC Entry point (Necessary for Spring)

	private MockMvc					mockMvc;

	// Controllers, converters and services in test

	@Autowired
	private MessageController		messageController;

	@Autowired
	private StringToActorConverter	stringToActorConverter;

	@Autowired
	private ActorToStringConverter	actorToStringConverter;

	// Web Application Context

	@Autowired
	private WebApplicationContext	context;


	// Setting up the test environment

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final FormattingConversionService cs = new FormattingConversionService();
		cs.addConverter(this.stringToActorConverter); // Adding necessary converters
		cs.addConverter(this.actorToStringConverter);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.messageController).setConversionService(cs).build();
		MockMvcBuilders.webAppContextSetup(this.context);
	}

	/*
	 * Test case: A user (customer/admin) must be able to send a message to another user if it is well formed.
	 * 
	 * A GET request is made to /message/send.do -> The user is prompted with the message form.
	 * 
	 * A POST request is made to /message/send.do with param "send" and the user input was valid ->
	 * The user is redirected to a list with his/her sent messages.
	 * 
	 * A POST request is made to /message/send.do with param "send" and the user input was invalid ->
	 * The user is redirected to the same form asking him/her to input valid data.
	 */

	@Test
	public void getSendTest() throws Exception {
		this.authenticate("customer1");

		RequestBuilder requestBuilder;

		requestBuilder = MockMvcRequestBuilders.get("/message/send", "");

		this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("message/send"));

		this.unauthenticate();
	}

	@Test
	public void postSendValidTest() throws Exception {
		final RequestBuilder requestBuilder;

		this.authenticate("customer1");

		requestBuilder = MockMvcRequestBuilders.post("/message/send", "send").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("title", "test").param("text", "test").param("attachments", "").param("recipient", "102").param("send", "");

		this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.model().hasNoErrors()).andExpect(MockMvcResultMatchers.model().attribute("message", "message.commit.ok")).andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/message/listSent.do")).andExpect(MockMvcResultMatchers.redirectedUrl("/message/listSent.do?message=message.commit.ok"));

		this.unauthenticate();
	}

	@Test
	public void postSendInvalidTest() throws Exception {
		final RequestBuilder requestBuilder;

		this.authenticate("customer1");

		requestBuilder = MockMvcRequestBuilders.post("/message/send", "send").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("title", "").param("text", "").param("attachments", "").param("recipient", "102").param("send", "");

		this.mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.model().hasErrors()).andExpect(MockMvcResultMatchers.view().name("message/send")).andExpect(MockMvcResultMatchers.status().isOk());

		this.unauthenticate();
	}
}
