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

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.api.discogs.DiscogsRelease;
import dev.pablito.dots.entity.DatabaseRelease;
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
	public DiscogsRelease getReleaseFromDiscogs(Long id) throws IOException, InterruptedException {
		return discogsClient.getRelease(id);
	}

	@Timed
	public void postRelease(DatabaseRelease release) {
		releaseRepository.insert(release);
	}

	@Timed
	public DatabaseRelease getRelease(Long id) {
		return releaseRepository.findById(id).orElse(null);
	}

	@Timed
	public void deleteReleases(List<Long> ids) {
		releaseRepository.deleteAllById(ids);
	}

	@Timed
	public void updateArchived(List<Long> ids, boolean archived) {
		List<DatabaseRelease> releases = releaseRepository.findAllById(ids);
		for (DatabaseRelease release : releases) {
			release.setArchived(archived);
			release.setDateLastEdition(LocalDateTime.now());
		}
		releaseRepository.saveAll(releases);
	}

	public boolean contains(Long id) {
		return releaseRepository.existsById(id);
	}

	public long countAllReleases() {
		return releaseRepository.count();
	}

	public long countReleasesByArchived(boolean archived) {
		return releaseRepository.countByArchived(archived);
	}

	public Optional<DatabaseRelease> updateReleaseNote(Long id, String note) {
		return releaseRepository.findById(id).map(release -> {
			release.setNote(note);
			return releaseRepository.save(release);
		});
	}

}
