
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.CommentableEntity;

@Repository
public interface CommentableEntityRepository extends JpaRepository<CommentableEntity, Integer> {

}
