package dev.pablito.dots.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import dev.pablito.dots.entity.DatabaseListing;
import dev.pablito.dots.entity.DatabaseProvider;
import dev.pablito.dots.entity.ProviderRequest;
import dev.pablito.dots.services.ListingService;
import dev.pablito.dots.services.ProviderService;

@RestController
@CrossOrigin
@RequestMapping("/dots")
public class ProviderController {
	
	@Autowired
	private ProviderService providerService;

	private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

	@Timed
	@PostMapping("/releases/{releaseId}/providers")
	public void createProvider(@PathVariable Long releaseId, @RequestBody ProviderRequest request)
			throws IOException, InterruptedException {
		try {
			providerService.createProvider(releaseId, request);
		} catch (Exception e) {
			logger.error("[TASK ERROR] createProvider({} {})", releaseId, request, e);
		}
	}
	
	@Timed
	@GetMapping("/releases/{releaseId}/providers")
	public ResponseEntity<List<DatabaseProvider>>getProviders(@PathVariable Long releaseId) throws IOException, InterruptedException {
        try {
        	List<DatabaseProvider> response = providerService.getProviders(releaseId);
            return new ResponseEntity<List<DatabaseProvider>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getProviders({})", releaseId, e);
            return ResponseEntity.noContent().build();
        }
	}

}
