package dev.pablito.dots.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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

import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.DatabaseRelease;
import dev.pablito.dots.entity.SearchRequest;
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
	
	@GetMapping("/buclePutRelease")
	public void buclePutRelease() throws IOException, InterruptedException {
	    releaseService.putReleaseFromDiscogs(33052227L);
	}


	// Gets all releases from database "Releases"
	@GetMapping("/getAllReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> getAllReleases(@PathVariable int page, @PathVariable int size)
			throws IOException, InterruptedException {
		Instant start = Instant.now();
		logger.info("[TASK START] getAllReleases({}, {})", page, size);
		try {
			Page<DatabaseRelease> response = releaseService.getAllReleases(page, size);
			Instant end = Instant.now();
			long duration = Duration.between(start, end).toSeconds();
			logger.info("[TASK END] getAllReleases({}, {}) - {} s ", page, size, duration);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getAllReleases({}, {})", page, size, e);
			return ResponseEntity.noContent().build();
		}
	}
	
	// Gets releases which archived = false from database "Releases"
	@GetMapping("/getUnarchivedReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> getUnarchivedReleases(@PathVariable int page, @PathVariable int size)
			throws IOException, InterruptedException {
		Instant start = Instant.now();
		logger.info("[TASK START] getUnarchivedReleases({}, {})", page, size);
		try {
			Page<DatabaseRelease> response = releaseService.getUnarchivedReleases(page, size);
			Instant end = Instant.now();
			long duration = Duration.between(start, end).toSeconds();
			logger.info("[TASK END] getUnarchivedReleases({}, {}) - {} s ", page, size, duration);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getUnarchivedReleases({}, {})", page, size, e);
			return ResponseEntity.noContent().build();
		}
	}
	
	// Gets releases which archived = true from database "Releases"
	@GetMapping("/getArchivedReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> getArchivedReleases(@PathVariable int page, @PathVariable int size)
			throws IOException, InterruptedException {
		Instant start = Instant.now();
		logger.info("[TASK START] getArchivedReleases({}, {})", page, size);
		try {
			Page<DatabaseRelease> response = releaseService.getArchivedReleases(page, size);
			Instant end = Instant.now();
			long duration = Duration.between(start, end).toSeconds();
			logger.info("[TASK END] getArchivedReleases({}, {}) - {} s ", page, size, duration);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getArchivedReleases({}, {})", page, size, e);
			return ResponseEntity.noContent().build();
		}
	}

	//TODO: Cambiar a get
	// Search all releases which in database "Releases"
	@PostMapping("/searchAllReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> searchAllReleases(@RequestBody SearchRequest request,
			@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		Instant start = Instant.now();
		logger.info("[TASK START] searchAllReleases({}, {}, {})", request.getSearch(), size, page);
		try {
			Page<DatabaseRelease> response = releaseService.searchReleases(request.getSearch(), page, size);
			Instant end = Instant.now();
			long duration = Duration.between(start, end).toSeconds();
			logger.info("[TASK END] searchAllReleases({}, {}, {}) - {} s ", request.getSearch(), size, page, duration);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] searchAllReleases({}, {}, {})", request.getSearch(), size, page, e);
			return ResponseEntity.noContent().build();
		}
	}
	
	//TODO: Cambiar a get
	// Search releases which archived = false in database "Releases"
	@PostMapping("/searchUnarchivedReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> searchUnarchivedReleases(@RequestBody SearchRequest request,
			@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		Instant start = Instant.now();
		logger.info("[TASK START] searchUnarchivedReleases({}, {}, {})", request.getSearch(), size, page);
		try {
			Page<DatabaseRelease> response = releaseService.searchReleasesByArchived(request.getSearch(), page, size, false);
			
			Instant end = Instant.now();
			long duration = Duration.between(start, end).toSeconds();
			logger.info("[TASK END] searchUnarchivedReleases({}, {}, {}) - {} s ", request.getSearch(), size, page, duration);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] searchUnarchivedReleases({}, {}, {})", request.getSearch(), size, page, e);
			return ResponseEntity.noContent().build();
		}
	}
	
	//TODO: Cambiar a get
	// Search releases which archived = true in database "Releases"
	@PostMapping("/searchArchivedReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> searchArchivedReleases(@RequestBody SearchRequest request,
			@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		Instant start = Instant.now();
		logger.info("[TASK START] searchArchivedReleases({}, {}, {})", request.getSearch(), size, page);
		try {
			Page<DatabaseRelease> response = releaseService.searchReleasesByArchived(request.getSearch(), page, size, true);
			Instant end = Instant.now();
			long duration = Duration.between(start, end).toSeconds();
			logger.info("[TASK END] searchArchivedReleases({}, {}, {}) - {} s ", request.getSearch(), size, page, duration);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] searchArchivedReleases({}, {}, {})", request.getSearch(), size, page, e);
			return ResponseEntity.noContent().build();
		}
	}
	
		
	// Delete selected releases identified by id which in database "Releases"
	@PostMapping("/deleteReleases")
	public void deleteReleases(@RequestBody List<Long> ids) {
		Instant start = Instant.now();
		logger.info("[TASK START] deleteReleases({})", ids);
		try {
			releaseService.deleteReleases(ids);
			Instant end = Instant.now();
			long duration = Duration.between(start, end).toSeconds();
			logger.info("[TASK END] deleteReleases({})", ids);
		} catch (Exception e) {
			logger.error("[TASK ERROR] deleteReleases({})", ids, e);
		}
		
	}
	
	// Archive selected releases identified by id which in database "Releases"
	@PostMapping("/archiveReleases")
	public void archiveReleases(@RequestBody List<Long> ids) {
		Instant start = Instant.now();
		logger.info("[TASK START] archiveReleases({})", ids);
		try {
			releaseService.archiveReleases(ids);
			Instant end = Instant.now();
			long duration = Duration.between(start, end).toSeconds();
			logger.info("[TASK END] archiveReleases({})", ids);
		} catch (Exception e) {
			logger.error("[TASK ERROR] archiveReleases({})", ids, e);
		}	
	}
	
	// Unarchive selected releases identified by id which in database "Releases"
	@PostMapping("/unarchiveReleases")
	public void unarchiveReleases(@RequestBody List<Long> ids) {
		Instant start = Instant.now();
		logger.info("[TASK START] unarchiveReleases({})", ids);
		try {
			releaseService.unarchiveReleases(ids);
			Instant end = Instant.now();
			long duration = Duration.between(start, end).toSeconds();
			logger.info("[TASK END] unarchiveReleases({})", ids);
		} catch (Exception e) {
			logger.error("[TASK ERROR] unarchiveReleases({})", ids, e);
		}	
	}

}
