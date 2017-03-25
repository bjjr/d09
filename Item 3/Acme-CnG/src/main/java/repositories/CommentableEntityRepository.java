
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CommentableEntity;

@Repository
public interface CommentableEntityRepository extends JpaRepository<CommentableEntity, Integer> {

	@Query("select ce from CommentableEntity ce where ce.id = ?1")
	CommentableEntity findById(Integer id);

}
