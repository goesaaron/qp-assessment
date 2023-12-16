package qp.assessment.grocerybooking.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroceryItemDTO {
    private Long itemId;
    private String name;
    private BigDecimal price;
    private Long quantity;
}
