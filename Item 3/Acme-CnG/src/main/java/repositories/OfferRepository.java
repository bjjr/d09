
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

	@Query("select count(o)/(selec count(c) from Customer c) from Offer o")
	Double findAvgOfferPerCostumer();

}
