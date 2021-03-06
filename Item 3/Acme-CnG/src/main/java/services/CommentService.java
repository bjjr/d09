
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import security.Authority;
import domain.Actor;
import domain.Comment;
import domain.CommentableEntity;
import domain.Trip;

@Service
@Transactional
public class CommentService {

	// Managed repository

	@Autowired
	private CommentRepository			commentRepository;

	// Supporting services

	@Autowired
	private ActorService				actorService;

	@Autowired
	private CommentableEntityService	commentableEntityService;

	@Autowired
	private TripService					tripService;

	@Autowired
	private Validator					validator;


	// Simple CRUD methods

	public Comment create() {
		final Comment res = new Comment();

		res.setTitle("");
		res.setMoment(new Date(System.currentTimeMillis() - 1000));
		res.setText("");
		res.setStars(0);
		res.setBanned(false);

		return res;
	}

	public Comment create(final Integer commentableEntityId) {
		final Comment res = new Comment();
		final Actor actor;
		final CommentableEntity commentableEntity;

		res.setTitle("");
		res.setMoment(new Date(System.currentTimeMillis() - 1000));
		res.setText("");
		res.setStars(0);
		res.setBanned(false);

		actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		commentableEntity = this.commentableEntityService.findOne(commentableEntityId);
		Assert.notNull(commentableEntity);

		res.setCommentableEntity(commentableEntity);
		res.setActor(actor);

		return res;
	}

	public Comment save(final Comment comment) {
		Assert.notNull(comment);

		Comment result;
		Date moment;
		Actor actor;

		actor = this.actorService.findByPrincipal();
		moment = new Date(System.currentTimeMillis() - 1000);
		comment.setMoment(moment);
		comment.setActor(actor);

		result = this.commentRepository.save(comment);

		return result;
	}

	public void flush() {
		this.commentRepository.flush();
	}

	public Comment banComment(final int commentId) {
		Assert.notNull(commentId);

		final Comment comment, result;
		Actor actor;
		final Authority auth;

		actor = this.actorService.findByPrincipal();
		auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(auth));

		comment = this.commentRepository.findOne(commentId);
		Assert.notNull(comment);

		comment.setBanned(true);
		result = this.commentRepository.save(comment);

		return result;
	}

	public Comment findOne(final int commentId) {
		final Comment res = this.commentRepository.findOne(commentId);
		Assert.notNull(res);

		return res;
	}

	public Comment reconstruct(final Comment comment, final BindingResult binding) {
		Comment result;
		Actor principal;

		result = null;

		if (comment.getId() == 0) {
			result = comment;
			principal = this.actorService.findByPrincipal();
			result.setActor(principal);
			result.setMoment(new Date(System.currentTimeMillis() - 1000));
			result.setBanned(false);
		}

		this.validator.validate(result, binding);

		return result;
	}

	public Comment reconstruct(final Comment comment, final int tripId, final BindingResult binding) {
		Comment result;
		Actor principal;
		final Trip trip;

		result = null;

		if (comment.getId() == 0) {
			result = comment;
			principal = this.actorService.findByPrincipal();
			result.setActor(principal);
			trip = this.tripService.findOne(tripId);
			Assert.notNull(trip);
			result.setCommentableEntity(trip);
			result.setMoment(new Date(System.currentTimeMillis() - 1000));
			result.setBanned(false);
		}

		this.validator.validate(result, binding);

		return result;
	}

	// Other business methods

	public Collection<Comment> findCommentsByCommentableEntity(final int commentableEntityId) {
		Assert.notNull(commentableEntityId);
		Assert.isTrue(commentableEntityId != 0);

		final Actor actor;
		final Authority auth;
		Collection<Comment> result;

		actor = this.actorService.findByPrincipal();
		auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		if (actor.getUserAccount().getAuthorities().contains(auth))
			result = this.commentRepository.findAdminCommentsByCommentableEntity(commentableEntityId);
		else
			result = this.commentRepository.findCommentsByCommentableEntity(commentableEntityId, actor.getId());
		Assert.notNull(result);

		return result;
	}

	public Collection<CommentableEntity> commentableEntities(final Actor actor) {
		Collection<CommentableEntity> result;
		Authority auth;

		result = new ArrayList<CommentableEntity>();
		auth = new Authority();
		auth.setAuthority(Authority.CUSTOMER);

		for (final Trip t : this.tripService.findAll())
			if (!result.contains(t.getCustomer()))
				result.add(t.getCustomer());

		return result;

	}

	public Double findAverageCommentPerActor() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.commentRepository.findAverageCommentPerActor();

		return result;
	}

	public Double findAverageCommentPerOffer() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.commentRepository.findAverageCommentPerOffer();

		return result;
	}

	public Double findAverageCommentPerRequest() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.commentRepository.findAverageCommentPerRequest();

		return result;
	}

	public Double findAverageCommentPerCustomer() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.commentRepository.findAverageCommentPerCustomer();

		return result;
	}

	public Double findAverageCommentPerAdministrator() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Double result;

		result = this.commentRepository.findAverageCommentPerAdministrator();

		return result;
	}

	public Collection<Comment> findCommentsOnActors() {
		Assert.isTrue(this.actorService.checkAuthority("ADMIN"));

		Collection<Comment> res;

		res = this.commentRepository.findCommentsOnActors();

		return res;
	}

}
