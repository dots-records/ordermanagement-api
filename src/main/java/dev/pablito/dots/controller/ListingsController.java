package dev.pablito.dots.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.DatabaseListing;
import dev.pablito.dots.entity.ListingRequest;
import dev.pablito.dots.services.ListingService;

@RestController
@CrossOrigin
@RequestMapping("/dots")
public class ListingsController {
	@Autowired
	private ListingService listingService;

	private static final Logger logger = LoggerFactory.getLogger(ListingsController.class);

	@Timed
	@PostMapping("/createListing/releaseId={releaseId}")
	public void createListing(@PathVariable Long releaseId)
			throws IOException, InterruptedException {
		try {
			listingService.createListing(releaseId);
		} catch (Exception e) {
			logger.error("[TASK ERROR] createListing({})", releaseId, e);
		}
	}
	
	@Timed
	@GetMapping("/getListings/releaseId={releaseId}")
	public ResponseEntity<List<DatabaseListing>>getListings(@PathVariable Long releaseId) throws IOException, InterruptedException {
        try {
        	List<DatabaseListing> response = listingService.getListings(releaseId);
            return new ResponseEntity<List<DatabaseListing>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getListings({})", releaseId, e);
            return ResponseEntity.noContent().build();
        }
	}

}
