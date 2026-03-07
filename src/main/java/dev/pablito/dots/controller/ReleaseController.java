package dev.pablito.dots.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.api.discogs.DiscogsRelease;
import dev.pablito.dots.entity.ArchiveRequest;
import dev.pablito.dots.entity.DatabaseRelease;
import dev.pablito.dots.entity.ReleaseRequest;
import dev.pablito.dots.mapper.ReleaseMapper;
import dev.pablito.dots.services.ReleaseService;

@RestController
@CrossOrigin
@RequestMapping("/dots")
public class ReleaseController {

	@Autowired
	private ReleaseService releaseService;

	private static final Logger logger = LoggerFactory.getLogger(ReleaseController.class);

	@Timed
	@GetMapping("/releases")
	public ResponseEntity<Page<DatabaseRelease>> getReleases(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int size, @RequestParam(required = false) Boolean archived,
			@RequestParam(required = false) String search) {
		try {
			Page<DatabaseRelease> response;
			if (search == null) {
				if (archived == null) {
					response = releaseService.getReleases(page, size);
				} else {
					response = releaseService.getReleasesByArchived(page, size, archived);
				}
			} else {
				if (archived == null) {
					response = releaseService.searchReleases(search, page, size);
				} else {
					response = releaseService.searchReleasesByArchived(search, page, size, archived);
				}
			}
			return new ResponseEntity<Page<DatabaseRelease>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getReleases({}, {}, {}, {}) ", page, size, archived, search, e);
			return ResponseEntity.noContent().build();
		}
	}

	@Timed
	@PostMapping("/releases")
	public void createRelease(@RequestBody ReleaseRequest request) throws IOException, InterruptedException {
		try {
			Long id = request.getDiscogsId();
			if (!releaseService.contains(id)) {
				DiscogsRelease discogsRelease = releaseService.getReleaseFromDiscogs(id);
				if (discogsRelease != null) {
					ReleaseMapper mapper = new ReleaseMapper();
					releaseService.postRelease(mapper.mapToDatabaseRelease(discogsRelease));
				}
			}
		} catch (Exception e) {
			logger.error("[TASK ERROR] createRelease({})", request.getDiscogsId(), e);
		}
	}

	// Gets release identified by "id" from database "Releases"
	@Timed
	@GetMapping("/releases/{id}")
	public ResponseEntity<DatabaseRelease> getRelease(@PathVariable Long id) throws IOException, InterruptedException {
		try {
			DatabaseRelease response = releaseService.getRelease(id);

			if (response == null) {
				return ResponseEntity.notFound().build();
			}

			return new ResponseEntity<DatabaseRelease>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getRelease({})", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Timed
	@DeleteMapping("/releases")
	public void deleteReleases(@RequestBody List<Long> ids) {
		try {
			releaseService.deleteReleases(ids);
		} catch (Exception e) {
			logger.error("[TASK ERROR] deleteReleases({})", ids, e);
		}

	}

	@Timed
	@PatchMapping("/releases/archived")
	public ResponseEntity<Void> updateArchived(@RequestBody ArchiveRequest request) {
		try {
			releaseService.updateArchived(request.getIds(), request.isArchived());
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] updateArchived({})", request, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@Timed
	@GetMapping("/releases/count")
	public ResponseEntity<Long> countReleases(@RequestParam(required = false) Boolean archived) {
		try {
			long count;
			if (archived == null) {
				count = releaseService.countAllReleases();
			} else {
				count = releaseService.countReleasesByArchived(archived);
			}
			return new ResponseEntity<>(count, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] countReleases({})", archived, e);
			return ResponseEntity.internalServerError().build();
		}
	}

	@Timed
	@PatchMapping("/releases/{id}/note")
	public ResponseEntity<Void> patchReleaseNote(@PathVariable Long id, @RequestBody Map<String, String> body) {
		try {
			String note = body.get("note");
			releaseService.updateReleaseNote(id, note);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchReleaseNote({}, body={})", id, body, e);
			return ResponseEntity.internalServerError().build();
		}
	}

}
