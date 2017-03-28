/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ActorServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private ActorService	actorService;


	// Tests ------------------------------------------------------------------

	/*
	 * Functional Requirement: Dashboard: The actors who have posted ±10% the average number of comments per actor
	 */

	@Test
	public void findActorPostMore10PercentAVGCommentsPerActorTest() {
		this.authenticate("admin");

		final Collection<Actor> res;
		List<Actor> expected, unexpected;

		expected = new ArrayList<>();
		unexpected = new ArrayList<>(this.actorService.findAll());
		res = this.actorService.findActorPostMore10PerCentAVGCommentsPerActor();

		expected.add(this.actorService.findOne(100));
		expected.add(this.actorService.findOne(101));

		unexpected.remove(this.actorService.findOne(100));
		unexpected.remove(this.actorService.findOne(101));

		Assert.isTrue(res.containsAll(expected));
		Assert.isTrue(res.retainAll(unexpected));
		Assert.isTrue(res.isEmpty());

		this.unauthenticate();
	}

	/*
	 * Functional Requirement: Dashboard: The actors who have posted ±10% the average number of comments per actor.
	 */

	@Test
	public void findActorPostLess10PercentAVGCommentsPerActorTest() {
		this.authenticate("admin");

		final Collection<Actor> res;

		res = this.actorService.findActorPostLess10PerCentAVGCommentsPerActor();

		Assert.isTrue(res.isEmpty());

		this.unauthenticate();
	}

	/*
	 * Functional Requirement: Dashboard: The actors who have sent more messages.
	 */

	@Test
	public void findActorWithMoreSentMessagesTest() {
		this.authenticate("admin");

		final Actor res;

		res = this.actorService.findActorWithMoreSentMessages();

		Assert.isTrue(res.equals(this.actorService.findOne(101)));

		this.unauthenticate();
	}

	/*
	 * Functional Requirement: Dashboard: The actors who have got more messages.
	 */

	@Test
	public void findActorWithMoreReceivedMessagesTest() {
		this.authenticate("admin");

		final Actor res;

		res = this.actorService.findActorWithMoreReceivedMessages();

		Assert.isTrue(res.equals(this.actorService.findOne(102)));

		this.unauthenticate();
	}
}
