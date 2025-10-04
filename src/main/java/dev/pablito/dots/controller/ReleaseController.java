package dev.pablito.dots.controller;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.api.discogs.DiscogsRelease;

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
import dev.pablito.dots.mapper.ReleaseMapper;
import dev.pablito.dots.services.ReleaseService;

@RestController
@CrossOrigin
@RequestMapping("/dots")
public class ReleaseController {

	@Autowired
	private ReleaseService releaseService;

	private static final Logger logger = LoggerFactory.getLogger(ReleaseController.class);

	// Puts release identified by id in Discogs in database "Release"
	@Timed
	@PostMapping("/putReleaseFromDiscogs/{id}")
	public void putReleaseFromDiscogs(@PathVariable Long id) throws IOException, InterruptedException {
		try {
			if(!releaseService.contains(id)) {
        		DiscogsRelease discogsRelease = releaseService.getReleaseFromDiscogs(id);
        		if(discogsRelease != null) {
            		ReleaseMapper mapper = new ReleaseMapper();
            		releaseService.postRelease(mapper.mapToDatabaseRelease(discogsRelease));
        		}
        	} 
		} catch (Exception e) {
			logger.error("[TASK ERROR] putReleaseFromDiscogs({})", id, e);
		}
	}
	
	// Gets release identified by "id" from database "Releases"
	@Timed
	@GetMapping("/getRelease/{id}")
	public ResponseEntity<DatabaseRelease>getRelease(@PathVariable Long id) throws IOException, InterruptedException {
        try {
        	DatabaseRelease response = releaseService.getRelease(id);
            return new ResponseEntity<DatabaseRelease>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getRelease({})", id, e);
            return ResponseEntity.noContent().build();
        }
	}

	// Gets all releases from database "Releases"
	@Timed
	@GetMapping("/getAllReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> getAllReleases(@PathVariable int page, @PathVariable int size)
			throws IOException, InterruptedException {
		try {
			Page<DatabaseRelease> response = releaseService.getAllReleases(page, size);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getAllReleases({}, {})", page, size, e);
			return ResponseEntity.noContent().build();
		}
	}
	
	// Gets releases which archived = false from database "Releases"
	@Timed
	@GetMapping("/getUnarchivedReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> getUnarchivedReleases(@PathVariable int page, @PathVariable int size)
			throws IOException, InterruptedException {
		try {
			Page<DatabaseRelease> response = releaseService.getUnarchivedReleases(page, size);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getUnarchivedReleases({}, {})", page, size, e);
			return ResponseEntity.noContent().build();
		}
	}
	
	// Gets releases which archived = true from database "Releases"
	@Timed
	@GetMapping("/getArchivedReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> getArchivedReleases(@PathVariable int page, @PathVariable int size)
			throws IOException, InterruptedException {
		try {
			Page<DatabaseRelease> response = releaseService.getArchivedReleases(page, size);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getArchivedReleases({}, {})", page, size, e);
			return ResponseEntity.noContent().build();
		}
	}

	//TODO: Cambiar a get
	// Search all releases which in database "Releases"
	@Timed
	@PostMapping("/searchAllReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> searchAllReleases(@RequestBody SearchRequest request,
			@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		try {
			Page<DatabaseRelease> response = releaseService.searchReleases(request.getSearch(), page, size);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] searchAllReleases({}, {}, {})", request.getSearch(), size, page, e);
			return ResponseEntity.noContent().build();
		}
	}
	
	//TODO: Cambiar a get
	// Search releases which archived = false in database "Releases"
	@Timed
	@PostMapping("/searchUnarchivedReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> searchUnarchivedReleases(@RequestBody SearchRequest request,
			@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		try {
			Page<DatabaseRelease> response = releaseService.searchReleasesByArchived(request.getSearch(), page, size, false);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] searchUnarchivedReleases({}, {}, {})", request.getSearch(), size, page, e);
			return ResponseEntity.noContent().build();
		}
	}
	
	//TODO: Cambiar a get
	// Search releases which archived = true in database "Releases"
	@Timed
	@PostMapping("/searchArchivedReleases/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseRelease>> searchArchivedReleases(@RequestBody SearchRequest request,
			@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		try {
			Page<DatabaseRelease> response = releaseService.searchReleasesByArchived(request.getSearch(), page, size, true);
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] searchArchivedReleases({}, {}, {})", request.getSearch(), size, page, e);
			return ResponseEntity.noContent().build();
		}
	}
	
		
	// Delete selected releases identified by id which in database "Releases"
	@Timed
	@PostMapping("/deleteReleases")
	public void deleteReleases(@RequestBody List<Long> ids) {
		try {
			releaseService.deleteReleases(ids);
		} catch (Exception e) {
			logger.error("[TASK ERROR] deleteReleases({})", ids, e);
		}
		
	}
	
	// Archive selected releases identified by id which in database "Releases"
	@Timed
	@PostMapping("/archiveReleases")
	public void archiveReleases(@RequestBody List<Long> ids) {
		try {
			releaseService.archiveReleases(ids);
		} catch (Exception e) {
			logger.error("[TASK ERROR] archiveReleases({})", ids, e);
		}	
	}
	
	// Unarchive selected releases identified by id which in database "Releases"
	@Timed
	@PostMapping("/unarchiveReleases")
	public void unarchiveReleases(@RequestBody List<Long> ids) {
		try {
			releaseService.unarchiveReleases(ids);
		} catch (Exception e) {
			logger.error("[TASK ERROR] unarchiveReleases({})", ids, e);
		}	
	}

}
