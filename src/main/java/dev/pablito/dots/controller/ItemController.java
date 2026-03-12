package dev.pablito.dots.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem;
import dev.pablito.dots.entity.ListingItemRequest;
import dev.pablito.dots.entity.ProviderItemRequest;
import dev.pablito.dots.entity.ReleaseItemRequest;
import dev.pablito.dots.services.ItemService;

@RestController
@CrossOrigin
@RequestMapping("/dots")
public class ItemController {
	@Autowired
	private ItemService itemService;
	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	@Timed
	@GetMapping("/orders/{orderId}/items/{itemId}")
	public ResponseEntity<?> getOrderItemListing(@PathVariable String orderId, @PathVariable String itemId) {
		try {
			DatabaseItem response = itemService.getOrderItemListing(orderId, itemId);
			return new ResponseEntity<DatabaseItem>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getOrderItemListing({}, {})", orderId, itemId);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{orderId}/items/{itemId}/listing")
	public ResponseEntity<?> patchOrderItemListing(@PathVariable String orderId, @PathVariable String itemId,
			@RequestBody ListingItemRequest request) {
		try {
			itemService.updateListingItem(orderId, itemId, request);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderItemListing({}, {}, {}, {})", orderId, itemId, request, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{orderId}/items/{itemId}/provider")
	public ResponseEntity<?> patchOrderItemProvider(@PathVariable String orderId, @PathVariable String itemId,
			@RequestBody ProviderItemRequest request) {
		try {
			itemService.updateProviderItem(orderId, itemId, request);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderItemProvider({}, {}, {}, {})", orderId, itemId, request, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{orderId}/items/{itemId}/release")
	public ResponseEntity<?> patchOrderItemRelease(@PathVariable String orderId, @PathVariable String itemId,
			@RequestBody ReleaseItemRequest request) {
		try {
			itemService.updateReleaseItem(orderId, itemId, request);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderItemRelease({}, {}, {}, {})", orderId, itemId, request, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{orderId}/items/{itemId}/associated")
	public ResponseEntity<?> patchOrderItemAssociated(@PathVariable String orderId, @PathVariable String itemId,
			@RequestBody Map<String, Boolean> body) {
		try {
			Boolean associated = body.get("associated");
			itemService.updateAssociatedItem(orderId, itemId, associated);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderItemAssociated({}, {}, {}, {})", orderId, itemId, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
