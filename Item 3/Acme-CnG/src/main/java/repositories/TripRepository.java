
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {

	@Query("select count(o)*1.0/(select count(r) from Request r) from Offer o")
	Double findRatioOfferVersusRequest();

	@Query("select t from Trip t where t.customer.id = ?1")
	Collection<Trip> findTripsByCustomer(int customerId);

	@Query("select t from Trip t where t.title like %?1% or t.description like %?1% or t.origin like %?1% or t.destination like %?1%")
	Collection<Trip> findByKeyword(String keyword);

	@Query("select t from Trip t where t.banned=FALSE")
	Collection<Trip> findAllNotBanned();

}
