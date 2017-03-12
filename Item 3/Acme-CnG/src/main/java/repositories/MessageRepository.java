
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message m where m.sender.id = ?1")
	Collection<Message> findMessagesBySender(int senderId);

	@Query("select m from Message m where m.recipient.id = ?1")
	Collection<Message> findMessagesByRecipient(int recipientId);

	@Query("select count(m) from Message m group by m.sender order by desc")
	List<Integer> findMinNumSntMsgPerActor();

	// TODO: Check this query
	@Query("select count(m)/(select count(a) from Actor a) from Message m")
	Double findAvgNumSntMsgPerActor();

	@Query("select count(m) from Message m group by m.sender order by asc")
	List<Integer> findMaxNumSntMsgPerActor();

	@Query("select count(m) from Message m group by m.recipient order by desc")
	List<Integer> findMinNumRecMsgPerActor();

	// TODO: Check this query
	@Query("select count(m)/(select count(a) from Actor a) from Message m")
	Double findAvgNumRecMsgPerActor();

	@Query("select count(m) from Message m group by m.recipient order by asc")
	List<Integer> findMaxNumRecMsgPerActor();

	@Query("select m.sender from Message m group by m.sender order by count(m) desc")
	Collection<Actor> findActorsWithMoreSentMessages();

	@Query("select m.recipient from Message m group by m.recipient order by count(m) desc")
	Collection<Actor> findActorsWithMoreReceivedMessages();

}
