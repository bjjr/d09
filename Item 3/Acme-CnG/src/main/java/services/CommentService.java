
package services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Actor;
import domain.Comment;

@Service
@Transactional
public class CommentService {

	// Managed repository

	@Autowired
	private CommentRepository	commentRepository;

	// Supporting services

	@Autowired
	private ActorService		actorService;


	// Simple CRUD methods

	public Comment create() {
		final Comment res = new Comment();
		final Actor actor;

		res.setTitle("");
		res.setMoment(new Date(System.currentTimeMillis() - 1000));
		res.setText("");
		res.setStars(0);
		res.setBanned(false);

		actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		// TODO
		// res.setCommentableEntity()
		res.setActor(actor);

		return res;
	}

	private Comment save(final Comment comment) {
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

	private Comment findOne(final int commentId) {
		final Comment res = this.commentRepository.findOne(commentId);
		Assert.notNull(res);

		return res;
	}
}
