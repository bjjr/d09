
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select count(c)*1.0/(select count(a) from Actor a) from Comment c join c.commentableEntity ca where type(ca)=domain.Administrator or type(ca)=domain.Customer")
	Double findAverageCommentPerActor();

	@Query("select count(c)*1.0/(select count(o) from Offer o) from Comment c join c.commentableEntity ca where type(ca)=domain.Offer")
	Double findAverageCommentPerOffer();

	@Query("select count(c)*1.0/(select count(r) from Request r) from Comment c join c.commentableEntity ca where type(ca)=domain.Request")
	Double findAverageCommentPerRequest();

	@Query("select count(c)*1.0/(select count(c) from Customer c) from Comment c join c.commentableEntity ca where type(ca)=domain.Customer")
	Double findAverageCommentPerCustomer();

	@Query("select count(c)*1.0/(select count(a) from Administrator a) from Comment c join c.commentableEntity ca where type(ca)=domain.Administrator")
	Double findAverageCommentPerAdministrator();

	@Query("select c from Comment c where c.commentableEntity.id = ?1 and (c.banned = false or c.actor.id = ?2)")
	Collection<Comment> findCommentsByCommentableEntity(int commentableEntityId, int actorId);

	@Query("select c from Comment c where c.commentableEntity.id = ?1")
	Collection<Comment> findAdminCommentsByCommentableEntity(int commentableEntityId);

	@Query("select c from Comment c where c.actor.id = ?1")
	Collection<Comment> findCommentsByActor(int actorId);

	@Query("select c from Comment c join c.commentableEntity ca where type(ca)=domain.Actor")
	Collection<Comment> findCommentsOnActors();

}
