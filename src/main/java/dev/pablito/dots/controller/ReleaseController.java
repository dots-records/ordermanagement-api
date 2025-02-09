package dev.pablito.dots.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.DatabaseRelease;
import dev.pablito.dots.services.ReleaseService;

@RestController
@CrossOrigin
@RequestMapping("/dots")
public class ReleaseController {
	
	@Autowired
	private ReleaseService releaseService;
	
	private static final Logger logger = LoggerFactory.getLogger(ReleaseController.class);
	
	// Puts release identified by id in Discogs in database "Release"
	@PostMapping("/putReleaseFromDiscogs/{id}")
	public void putReleaseFromDiscogs(@PathVariable Long id) throws IOException, InterruptedException {
		Instant start = Instant.now();
        logger.info("[TASK START] putReleaseFromDiscogs({})", id);
        try {
        	releaseService.putReleaseFromDiscogs(id);
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] putReleaseFromDiscogs({}) - {} s ", id, duration);
        } catch (Exception e) {
            logger.error("[TASK ERROR] putReleaseFromDiscogs({})", id, e);
        }
	}
	
	// Gets all releases from database "Releases"
	@GetMapping("/getReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>>getReleases(@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		Instant start = Instant.now();
        logger.info("[TASK START] getReleases({}, {})", page, size);
        try {
        	Page<DatabaseRelease> response = releaseService.getReleases(page, size);
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] getReleases({}, {}) - {} s ", page, size, duration);
            return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getReleases({}, {})", page, size, e);
            return ResponseEntity.noContent().build();
        }
	}

}
