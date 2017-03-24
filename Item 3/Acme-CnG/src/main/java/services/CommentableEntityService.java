
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentableEntityRepository;
import domain.CommentableEntity;

@Transactional
@Service
public class CommentableEntityService {

	//Managed repository

	@Autowired
	private CommentableEntityRepository	commentableEntityRepository;


	public CommentableEntity findOne(final int id) {
		final CommentableEntity res = this.commentableEntityRepository.findById(id);
		Assert.notNull(res, "CommentableEntityService.findOne: CommentableEntity cannot be null");
		return res;
	}

	public CommentableEntity save(final CommentableEntity ce) {
		CommentableEntity res;
		Assert.notNull(ce);

		res = this.commentableEntityRepository.save(ce);
		Assert.notNull(res);

		return res;
	}

	public void flush() {
		this.commentableEntityRepository.flush();
	}

}
