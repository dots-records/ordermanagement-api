package dev.pablito.dots.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.api.discogs.DiscogsRelease;
import dev.pablito.dots.entity.DatabaseRelease;
import dev.pablito.dots.entity.ReleaseRequest;
import dev.pablito.dots.exceptions.DiscogsException;
import dev.pablito.dots.exceptions.InvalidException;
import dev.pablito.dots.exceptions.MongoException;
import dev.pablito.dots.exceptions.NotFoundException;
import dev.pablito.dots.mapper.ReleaseMapper;
import dev.pablito.dots.repository.ReleaseRepository;

@Service
public class ReleaseService {
	@Autowired
	private ReleaseRepository releaseRepository;
	@Autowired
	private DiscogsClient discogsClient;

	private static final Logger logger = LoggerFactory.getLogger(ReleaseService.class);

	@Timed
	public Page<DatabaseRelease> getReleases(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateLastEdition"));
		Page<DatabaseRelease> releasePage = releaseRepository.findAll(pageable);
		return releasePage;
	}

	@Timed
	public Page<DatabaseRelease> getReleasesByArchived(int page, int size, boolean archived)
			throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateLastEdition"));
		Page<DatabaseRelease> releasePage = releaseRepository.findByArchived(archived, pageable);
		return releasePage;
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void createReleaseFromDiscogs(ReleaseRequest request) throws IOException, InterruptedException,
			NotFoundException, InvalidException, MongoException, DiscogsException {
		Long id = request.getDiscogsId();
		if (!releaseRepository.existsById(id)) {
			DiscogsRelease discogsRelease = discogsClient.getRelease(id);
			if (discogsRelease != null) {
				ReleaseMapper mapper = new ReleaseMapper();
				try {
					releaseRepository.insert(mapper.mapToDatabaseRelease(discogsRelease));
				} catch (Exception e) {
					throw new MongoException("Error saving the release in MongoDB: " + e.getMessage());
				}

			} else {
				throw new NotFoundException("Release not found in Discogs");
			}
		} else {
			throw new InvalidException("Release already added");
		}
	}

	@Timed
	public Page<DatabaseRelease> searchReleases(String search, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateLastEdition"));
		return releaseRepository.findBySearchTerm(search, pageable);
	}

	@Timed
	public Page<DatabaseRelease> searchReleasesByArchived(String palabra, int page, int size, boolean archived) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateLastEdition"));
		return releaseRepository.findByArchivedAndSearchTerm(palabra, archived, pageable);
	}

	@Timed
	public DatabaseRelease getRelease(Long id) {
		Optional<DatabaseRelease> optRelease = releaseRepository.findById(id);
		if (optRelease.isPresent()) {
			return optRelease.get();
		} else {
			return null;
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void deleteReleases(List<Long> ids) throws MongoException {
		try {
			releaseRepository.deleteAllById(ids);
		} catch (Exception e) {
			throw new MongoException("Error deleting the release/s in MongoDB: " + e.getMessage());
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateArchived(List<Long> ids, boolean archived) throws MongoException {
		List<DatabaseRelease> releases = releaseRepository.findAllById(ids);
		for (DatabaseRelease release : releases) {
			release.setArchived(archived);
			release.setDateLastEdition(LocalDateTime.now());
		}
		try {
			releaseRepository.saveAll(releases);
		} catch (Exception e) {
			throw new MongoException("Error updating archived of the release/s in MongoDB: " + e.getMessage());
		}
	}

	@Timed
	public long countAllReleases() {
		return releaseRepository.count();
	}

	@Timed
	public long countReleasesByArchived(boolean archived) {
		return releaseRepository.countByArchived(archived);
	}

	@Timed
	public boolean contains(Long id) {
		return releaseRepository.existsById(id);
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateReleaseNote(Long id, String note) throws MongoException, NotFoundException {
		if (releaseRepository.existsById(id)) {
			try {
				releaseRepository.findById(id).map(release -> {
					release.setNote(note);
					return releaseRepository.save(release);
				});
			} catch (Exception e) {
				throw new MongoException("Error updating note of the release in MongoDB: " + e.getMessage());
			}
		} else {
			throw new NotFoundException("Release not found in MongoDB");
		}

	}

}
