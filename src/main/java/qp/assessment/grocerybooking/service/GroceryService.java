package qp.assessment.grocerybooking.service;

import qp.assessment.grocerybooking.exception.BadRequestException;
import qp.assessment.grocerybooking.exception.InternalServerException;
import qp.assessment.grocerybooking.exception.ResourceNotFoundException;
import qp.assessment.grocerybooking.model.GroceryItem;
import qp.assessment.grocerybooking.model.GroceryItemDTO;
import qp.assessment.grocerybooking.model.OrderDTO;

import java.util.List;
import java.util.Map;

public interface GroceryService {
    List<GroceryItem> getAllItems();
    GroceryItem getItemById(Long id) throws ResourceNotFoundException, InternalServerException;
    void addItem(GroceryItemDTO item) throws BadRequestException, InternalServerException;
    void updateItem(Long id, GroceryItemDTO updatedItem) throws ResourceNotFoundException, InternalServerException;
    void removeItem(Long id) throws ResourceNotFoundException, BadRequestException, InternalServerException;
    Map<Long,String> bookItems(List<OrderDTO> orderDTOList);
}
