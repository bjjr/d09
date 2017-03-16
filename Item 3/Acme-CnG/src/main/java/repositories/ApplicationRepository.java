
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select count(a)/(select count(o) from Offer o) from Application a where TYPE(a.trip)=domain.Offer")
	Double findAvgApplicationsPerOffer();

	@Query("select count(a)/(select count(r) from Request r) from Application a where TYPE(a.trip)=domain.Request")
	Double findAvgApplicationsPerRequest();

	@Query("select a from Application a where a.customer.id = ?1")
	Collection<Application> findApplicationsByCustomer(int customerId);

	@Query("select a from Application a where a.trip.id = ?1")
	Collection<Application> findApplicationsByTrip(int tripId);

}
