package qp.assessment.grocerybooking.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import qp.assessment.grocerybooking.exception.BadRequestException;
import qp.assessment.grocerybooking.exception.InternalServerException;
import qp.assessment.grocerybooking.exception.ResourceNotFoundException;
import qp.assessment.grocerybooking.model.GroceryItem;
import qp.assessment.grocerybooking.model.GroceryItemDTO;
import qp.assessment.grocerybooking.model.OrderDTO;
import qp.assessment.grocerybooking.repository.GroceryBookingRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroceryServiceImpl implements GroceryService {

    @Autowired
    private GroceryBookingRepository groceryBookingRepository;

    @Override
    public List<GroceryItem> getAllItems() {
        return (List<GroceryItem>) groceryBookingRepository.findAll();
    }

    @Override
    public GroceryItem getItemById(Long id) throws ResourceNotFoundException, InternalServerException {
        Optional<GroceryItem> optionalGroceryItem;
        try {
            optionalGroceryItem = groceryBookingRepository.findById(id);
        } catch (Exception e) {
            throw new InternalServerException("Error while finding groceryItem by Id : + " + id);
        }

        if (!optionalGroceryItem.isPresent()) {
            throw new ResourceNotFoundException("Id :" + id + " not found");
        }
        return optionalGroceryItem.get();

    }

    @Override
    public void addItem(GroceryItemDTO item) throws BadRequestException, InternalServerException {
        if (item == null) {
            throw new BadRequestException("Empty Request");
        }

        GroceryItem groceryItem = new GroceryItem();
        BeanUtils.copyProperties(item, groceryItem);
        saveGroceryItem(groceryItem);
    }

    @Override
    public void updateItem(Long id, GroceryItemDTO updatedItem) throws ResourceNotFoundException, InternalServerException {

        GroceryItem existingItem = getItemById(id);
        if (updatedItem.getName() != null) {
            existingItem.setName(updatedItem.getName());
        }
        if (updatedItem.getPrice() != null) {
            existingItem.setPrice(updatedItem.getPrice());
        }
        if (updatedItem.getQuantity() != null) {
            existingItem.setQuantity(updatedItem.getQuantity());
        }
        saveGroceryItem(existingItem);

    }

    @Override
    public void removeItem(Long id) throws ResourceNotFoundException, BadRequestException, InternalServerException {
        if (id == null) {
            throw new BadRequestException("Id cannot be empty");
        }
        try {
            groceryBookingRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id :" + id + " not found");
        } catch (Exception e) {
            throw new InternalServerException("Error while deleting item");
        }

    }

    @Override
    public Map<Long, String> bookItems(List<OrderDTO> orderDTOList) {

        Map<Long, String> errorMap = new HashMap<>();
        if (orderDTOList == null || orderDTOList.isEmpty()) {
            return errorMap;
        }

        List<Long> allOrderItemIds = orderDTOList.stream().map(OrderDTO::getItemId).collect(Collectors.toList());
        Iterable<GroceryItem> groceryItemsListFromDb = groceryBookingRepository.findAllById(allOrderItemIds);
        Map<Long, GroceryItem> groceryItemMap = convertIterableToMap(groceryItemsListFromDb);

        List<GroceryItem> updatedGroceryItemList = new ArrayList<>();

        for (OrderDTO orderDTO : orderDTOList) {
            GroceryItem groceryItem = groceryItemMap.get(orderDTO.getItemId());

            if (groceryItem == null) {
                errorMap.put(orderDTO.getItemId(), "Grocery item not found");
            } else if (orderDTO.getQuantity() > groceryItem.getQuantity()) {
                errorMap.put(orderDTO.getItemId(), "InSufficient quantity");
            } else {
                groceryItem.setQuantity(groceryItem.getQuantity() - orderDTO.getQuantity());
                updatedGroceryItemList.add(groceryItem);
            }

        }

        if (!updatedGroceryItemList.isEmpty()) {
            groceryBookingRepository.saveAll(updatedGroceryItemList);
        }

        return errorMap;
    }

    private void saveGroceryItem(GroceryItem groceryItem) throws InternalServerException {
        try {
            groceryBookingRepository.save(groceryItem);
        } catch (Exception e) {
            throw new InternalServerException("Error while saving/updating item");
        }
    }

    private static Map<Long, GroceryItem> convertIterableToMap(Iterable<GroceryItem> groceryItems) {
        Map<Long, GroceryItem> groceryItemMap = new HashMap<>();

        for (GroceryItem groceryItem : groceryItems) {
            groceryItemMap.put(groceryItem.getItemId(), groceryItem);
        }
        return groceryItemMap;
    }
}
