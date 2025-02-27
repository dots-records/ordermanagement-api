package dev.pablito.dots.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.ListingRequest;
import dev.pablito.dots.services.ListingService;

public class ListingsController {
	@Autowired
	private ListingService listingService;

	private static final Logger logger = LoggerFactory.getLogger(ListingsController.class);

	@Timed
	@PostMapping("/createListing/releaseId={releaseId}")
	public void createListing(@PathVariable Long releaseId, @RequestBody ListingRequest request)
			throws IOException, InterruptedException {
		try {
			Page<DatabaseOrder> response = orderService.getAllOrders(page, size);
		} catch (Exception e) {
			logger.error("[TASK ERROR] createListing({})", releaseId, e);
		}
	}

}
