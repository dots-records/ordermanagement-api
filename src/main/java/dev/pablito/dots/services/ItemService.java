package dev.pablito.dots.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pablito.dots.controller.ItemController;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem.DatabaseListingItem;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem.DatabaseProviderItem;
import dev.pablito.dots.entity.ListingItemRequest;
import dev.pablito.dots.entity.ProviderItemRequest;
import dev.pablito.dots.repository.OrderRepository;

@Service
public class ItemService {

	@Autowired
	private OrderRepository orderRepository;

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	public void updateListingItem(String orderId, String itemId, ListingItemRequest request) {
		Optional<DatabaseOrder> optOrder = orderRepository.findById(orderId);
		if (!optOrder.isEmpty()) {
			DatabaseOrder order = optOrder.get();
			DatabaseOrder.DatabaseItem searchedItem = order.getItems().stream()
					.filter(item -> item.getId() != null && item.getId().equals(itemId)).findFirst().orElse(null);
			if (searchedItem != null) {
				DatabaseOrder.DatabaseItem.DatabaseListingItem listing = new DatabaseListingItem(request.getId(),
						request.getPlatform(), request.getLink(), request.getSellingPrice());
				searchedItem.setListing(listing);
				orderRepository.save(order);
			}
		}

	}

	public void updateProviderItem(String orderId, String itemId, ProviderItemRequest request) {
		Optional<DatabaseOrder> optOrder = orderRepository.findById(orderId);
		if (!optOrder.isEmpty()) {
			DatabaseOrder order = optOrder.get();
			DatabaseOrder.DatabaseItem searchedItem = order.getItems().stream()
					.filter(item -> item.getId() != null && item.getId().equals(itemId)).findFirst().orElse(null);
			if (searchedItem != null) {
				DatabaseOrder.DatabaseItem.DatabaseProviderItem provider = new DatabaseProviderItem(request.getId(),
						request.getType(), request.getPrice(), request.getLink(), request.getUnits(),
						request.getDiscCondition(), request.getSleeveCondition(), request.getDescription());
				searchedItem.setProvider(provider);
				orderRepository.save(order);
			}
		}
	}

	public void updateAssociatedItem(String orderId, String itemId, Boolean associated) {
		Optional<DatabaseOrder> optOrder = orderRepository.findById(orderId);
		if (!optOrder.isEmpty()) {
			DatabaseOrder order = optOrder.get();
			DatabaseOrder.DatabaseItem searchedItem = order.getItems().stream()
					.filter(item -> item.getId() != null && item.getId().equals(itemId)).findFirst().orElse(null);
			if (searchedItem != null) {
				searchedItem.setAssociated(associated);
				orderRepository.save(order);
			}
		}
	}

}