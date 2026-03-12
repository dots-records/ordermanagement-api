package dev.pablito.dots.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.controller.ItemController;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem.DatabaseListingItem;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem.DatabaseProviderItem;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem.DatabaseReleaseItem;
import dev.pablito.dots.entity.ListingItemRequest;
import dev.pablito.dots.entity.ProviderItemRequest;
import dev.pablito.dots.entity.ReleaseItemRequest;
import dev.pablito.dots.exceptions.MongoException;
import dev.pablito.dots.exceptions.NotFoundException;

@Service
public class ItemService {

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateListingItem(String orderId, String itemId, ListingItemRequest request) throws MongoException {
		DatabaseListingItem newListing = new DatabaseListingItem(request.getId(), request.getPlatform(),
				request.getLink(), request.getSellingPrice());

		Query query = new Query(Criteria.where("_id").is(orderId).and("items.id").is(itemId));
		Update update = new Update().set("items.$.listing", newListing);
		try {
			mongoTemplate.updateFirst(query, update, DatabaseOrder.class);
		} catch (Exception e) {
			throw new MongoException("Error saving the item's listing " + e.getMessage());
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateProviderItem(String orderId, String itemId, ProviderItemRequest request) throws MongoException {
		DatabaseProviderItem newProvider = new DatabaseProviderItem(request.getId(), request.getType(),
				request.getPrice(), request.getLink(), request.getUnits(), request.getDiscCondition(),
				request.getSleeveCondition(), request.getDescription());

		Query query = new Query(Criteria.where("_id").is(orderId).and("items.id").is(itemId));
		Update update = new Update().set("items.$.provider", newProvider);
		try {
			mongoTemplate.updateFirst(query, update, DatabaseOrder.class);
		} catch (Exception e) {
			throw new MongoException("Error saving the item's provider " + e.getMessage());
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateReleaseItem(String orderId, String itemId, ReleaseItemRequest request) throws Exception {
		DatabaseReleaseItem newRelease = new DatabaseReleaseItem(request.getId(), request.getName(),
				request.getArtists(), request.getThumb());

		Query query = new Query(Criteria.where("_id").is(orderId).and("items.id").is(itemId));
		Update update = new Update().set("items.$.release", newRelease);
		try {
			mongoTemplate.updateFirst(query, update, DatabaseOrder.class);
		} catch (Exception e) {
			throw new MongoException("Error saving the item's release");
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateAssociatedItem(String orderId, String itemId, Boolean associated) throws MongoException {
		Query query = new Query(Criteria.where("_id").is(orderId).and("items.id").is(itemId));
		Update update = new Update().set("items.$.associated", associated);
		try {
			mongoTemplate.updateFirst(query, update, DatabaseOrder.class);
		} catch (Exception e) {
			throw new MongoException("Error saving the item's associated " + e.getMessage());
		}

	}

	public DatabaseItem getOrderItemListing(String orderId, String itemId) throws NotFoundException {

		Query query = new Query(
				Criteria.where("_id").is(orderId).and("items").elemMatch(Criteria.where("id").is(itemId)));

		query.fields().include("items.$");

		DatabaseOrder order = mongoTemplate.findOne(query, DatabaseOrder.class);

		if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
			throw new NotFoundException("Item not found");
		}

		return order.getItems().get(0);
	}

}