
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("select c from Customer c where c.userAccount.id = ?1")
	Customer findByUserAccountId(int userAccountId);

	@Query("select a.customer.id from Application a where a.status like 'ACCEPTED' group by a.customer order by count(a) desc")
	Customer findCustomerWhoHasMoreApplicationsAccepted();

	@Query("select a.customer.id from Application a where a.status like 'DENIED' group by a.customer order by count(a) desc")
	Customer findCustomerWhoHasMoreApplicationsDenied();

}
