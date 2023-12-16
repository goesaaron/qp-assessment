package qp.assessment.grocerybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qp.assessment.grocerybooking.exception.BadRequestException;
import qp.assessment.grocerybooking.exception.InternalServerException;
import qp.assessment.grocerybooking.exception.ResourceNotFoundException;
import qp.assessment.grocerybooking.model.GroceryItem;
import qp.assessment.grocerybooking.model.GroceryItemDTO;
import qp.assessment.grocerybooking.model.OrderDTO;
import qp.assessment.grocerybooking.service.GroceryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class GroceryController {

    @Autowired
    private GroceryService groceryService;

    @GetMapping("/items")
    public ResponseEntity<List<GroceryItem>> getAllItems() {
        List<GroceryItem> items = groceryService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(@RequestBody GroceryItemDTO item) throws BadRequestException, InternalServerException {
        groceryService.addItem(item);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Void> updateItem(@PathVariable Long id, @RequestBody GroceryItemDTO updatedItem) throws ResourceNotFoundException, InternalServerException {
        groceryService.updateItem(id, updatedItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException, InternalServerException {
        groceryService.removeItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<Map<Long, String>> bookItems(@RequestBody List<OrderDTO> orderDTOList) {
        Map<Long, String> errorMap = groceryService.bookItems(orderDTOList);
        return new ResponseEntity<>(errorMap, HttpStatus.OK);
    }
}
