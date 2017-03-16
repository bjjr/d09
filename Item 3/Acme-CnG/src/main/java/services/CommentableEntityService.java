
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.CommentableEntityRepository;
import domain.CommentableEntity;

@Transactional
@Service
public class CommentableEntityService {

	@Autowired
	private CommentableEntityRepository	commentableEntityRepository;


	public CommentableEntity findOne(final int id) {
		return this.commentableEntityRepository.findOne(id);
	}

}
