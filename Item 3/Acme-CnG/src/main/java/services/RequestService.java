
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.Application;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;

	// Supporting services -----------------------------------------------------

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private ActorService		actorService;


	// Constructors -----------------------------------------------------

	public RequestService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Request create() {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));

		Request result;
		Date moment;
		Boolean banned;

		result = new Request();
		moment = new Date(System.currentTimeMillis() - 1000);
		banned = false;

		result.setMoment(moment);
		result.setBanned(banned);

		return result;
	}

	public Request findOne(final int requestId) {
		Assert.notNull(requestId);
		Assert.isTrue(requestId != 0);

		Request result;

		result = this.requestRepository.findOne(requestId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Request> findAll() {
		Collection<Request> result;

		result = this.requestRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public void flush() {
		this.requestRepository.flush();
	}

	// Other business methods ----------------------------------------------------

	public Collection<Request> findByKeyword(final String keyword) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));
		Assert.notNull(keyword);

		Collection<Request> result;

		result = this.requestRepository.findByKeyword(keyword);
		Assert.notNull(result);

		return result;

	}

	public Double findAvgRequestCustomer() {
		Assert.isTrue(this.actorService.checkAuthority("ADMINISTRATOR"));

		Double result;

		result = this.requestRepository.findAvgRequestCustomer();
		Assert.notNull(result);

		return result;
	}

	public Collection<Request> findAllNotBanned() {
		Collection<Request> result;

		result = this.requestRepository.findAllNotBanned();
		Assert.notNull(result);

		return result;
	}

	public void accept(final Application application) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));
		Assert.notNull(application);
		Assert.isTrue(application.getStatus() == "PENDING");

		application.setStatus("ACCEPTED");

	}

	public void deny(final Application application) {
		Assert.isTrue(this.actorService.checkAuthority("CUSTOMER"));
		Assert.notNull(application);
		Assert.isTrue(application.getStatus() == "PENDING");

		application.setStatus("DENIED");

	}

	public void ban(final Request request) {
		Assert.isTrue(this.actorService.checkAuthority("ADMINISTRATOR"));
		Assert.notNull(request);

		Collection<Application> applications;

		applications = this.applicationService.findApplicationsByTrip(request.getId());

		request.setBanned(true);

		for (final Application ap : applications)
			this.applicationService.delete(ap);

	}

}
