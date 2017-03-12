
package services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.CommentRepository;
import domain.Comment;

@Service
@Transactional
public class CommentService {

	// Managed repository

	@Autowired
	private CommentRepository	commentRepository;


	// Supporting services

	// Simple CRUD methods

	public Comment create() {
		final Comment res = new Comment();

		res.setTitle("");
		res.setMoment(new Date(System.currentTimeMillis() - 1000));
		res.setText("");
		res.setStars(0);
		res.setBanned(false);

		// TODO
		// res.setCommentableEntity()
		// res.setActor()

		return res;
	}

}
