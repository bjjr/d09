
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

	@Query("select m from MessageEntity m where m.sender.id = ?1")
	Collection<MessageEntity> findMessagesBySender(int senderId);

	@Query("select m from MessageEntity m where m.recipient.id = ?1")
	Collection<MessageEntity> findMessagesByRecipient(int recipientId);

	@Query("select count(m) from MessageEntity m group by m.sender order by count(m) asc")
	List<Long> findMinNumSntMsgPerActor();

	@Query("select count(m)*1.0/(select count(a) from Actor a) from MessageEntity m")
	Double findAvgNumSntMsgPerActor();

	@Query("select count(m) from MessageEntity m group by m.sender order by count(m) desc")
	List<Long> findMaxNumSntMsgPerActor();

	@Query("select count(m) from MessageEntity m group by m.recipient order by count(m) asc")
	List<Long> findMinNumRecMsgPerActor();

	@Query("select count(m)*1.0/(select count(a) from Actor a) from MessageEntity m")
	Double findAvgNumRecMsgPerActor();

	@Query("select count(m) from MessageEntity m group by m.recipient order by count(m) desc")
	List<Long> findMaxNumRecMsgPerActor();

}
