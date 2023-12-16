package qp.assessment.grocerybooking.model;


/*import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;*/
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class GroceryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemId;
    private String name;
    private BigDecimal price;
    private long quantity;
}
