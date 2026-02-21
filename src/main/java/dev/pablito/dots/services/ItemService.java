package dev.pablito.dots.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import dev.pablito.dots.controller.ItemController;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem.DatabaseListingItem;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem.DatabaseProviderItem;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem.DatabaseReleaseItem;
import dev.pablito.dots.entity.ListingItemRequest;
import dev.pablito.dots.entity.ProviderItemRequest;
import dev.pablito.dots.entity.ReleaseItemRequest;

@Service
public class ItemService {

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	public void updateListingItem(String orderId, String itemId, ListingItemRequest request) {
		DatabaseListingItem newListing = new DatabaseListingItem(request.getId(), request.getPlatform(),
				request.getLink(), request.getSellingPrice());

		Query query = new Query(Criteria.where("_id").is(orderId).and("items.id").is(itemId));
		Update update = new Update().set("items.$.listing", newListing);

		mongoTemplate.updateFirst(query, update, DatabaseOrder.class);
	}

	public void updateProviderItem(String orderId, String itemId, ProviderItemRequest request) {
		DatabaseProviderItem newProvider = new DatabaseProviderItem(request.getId(), request.getType(),
				request.getPrice(), request.getLink(), request.getUnits(), request.getDiscCondition(),
				request.getSleeveCondition(), request.getDescription());

		Query query = new Query(Criteria.where("_id").is(orderId).and("items.id").is(itemId));
		Update update = new Update().set("items.$.provider", newProvider);

		mongoTemplate.updateFirst(query, update, DatabaseOrder.class);
	}

	public void updateReleaseItem(String orderId, String itemId, ReleaseItemRequest request) {
		DatabaseReleaseItem newRelease = new DatabaseReleaseItem(request.getId(), request.getName(),
				request.getArtists(), request.getThumb());

		Query query = new Query(Criteria.where("_id").is(orderId).and("items.id").is(itemId));
		Update update = new Update().set("items.$.release", newRelease);

		mongoTemplate.updateFirst(query, update, DatabaseOrder.class);
	}

	public void updateAssociatedItem(String orderId, String itemId, Boolean associated) {
		Query query = new Query(Criteria.where("_id").is(orderId).and("items.id").is(itemId));
		Update update = new Update().set("items.$.associated", associated);

		mongoTemplate.updateFirst(query, update, DatabaseOrder.class);
	}

}