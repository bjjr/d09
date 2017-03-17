
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Actor;
import domain.Comment;
import domain.CommentableEntity;

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


	// Simple CRUD methods

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
		final Comment res;
		final Actor actor;

		actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		if (comment.getId() != 0)
			Assert.isTrue(comment.getActor().equals(actor));

		res = this.commentRepository.save(comment);
		return res;
	}

	public Comment findOne(final int commentId) {
		final Comment res = this.commentRepository.findOne(commentId);
		Assert.notNull(res);

		return res;
	}

	// Other business methods

	public Collection<Comment> findCommentsByCommentableEntity(final int commentableEntityId) {
		Assert.notNull(commentableEntityId);
		Assert.isTrue(commentableEntityId != 0);

		Collection<Comment> result;

		result = this.commentRepository.findCommentsByCommentableEntity(commentableEntityId);
		Assert.notNull(result);

		return result;
	}

}
