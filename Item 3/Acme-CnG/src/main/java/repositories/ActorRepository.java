
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select c.actor from Comment c group by c.actor having count(c) > 0.1*(select count(co)/(select count(a) from Actor a) from Comment co)")
	Collection<Actor> findActorPostMore10PerCentAVGCommentsPerActor();

	@Query("select c.actor from Comment c group by c.actor having count(c) < 0.1*(select count(co)/(select count(a) from Actor a) from Comment co)")
	Collection<Actor> findActorPostLess10PerCentAVGCommentsPerActor();

	@Query("select m.sender from Message m group by m.sender order by count(m) desc")
	List<Actor> findActorWithMoreSentMessages();

	@Query("select m.recipient from Message m group by m.recipient order by count(m) desc")
	List<Actor> findActorWithMoreReceivedMessages();

	@Query("select a from Actor a where a.userAccount.id = ?1")
	Actor findByUserAccountId(int userAccountId);

}
