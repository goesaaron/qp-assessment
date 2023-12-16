package qp.assessment.grocerybooking.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import qp.assessment.grocerybooking.model.GroceryItem;


@Repository
public interface GroceryBookingRepository extends CrudRepository<GroceryItem, Long> {

}
