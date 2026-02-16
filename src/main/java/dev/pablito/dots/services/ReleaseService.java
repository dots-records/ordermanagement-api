package dev.pablito.dots.services;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	public DiscogsRelease getReleaseFromDiscogs(Long id) throws IOException, InterruptedException {
		return discogsClient.getRelease(id);
	}

	@Timed
	public void postRelease(DatabaseRelease release) {
		releaseRepository.insert(release);
	}

	@Timed
	public Page<DatabaseRelease> getAllReleases(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size);
		Page<DatabaseRelease> releasePage = releaseRepository.findAll(pageable);
		return releasePage;
	}

	@Timed
	public Page<DatabaseRelease> getArchivedReleases(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size);
		Page<DatabaseRelease> releasePage = releaseRepository.findByArchived(true, pageable);
		return releasePage;
	}

	@Timed
	public Page<DatabaseRelease> getUnarchivedReleases(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size);
		Page<DatabaseRelease> releasePage = releaseRepository.findByArchived(false, pageable);
		return releasePage;
	}

	@Timed
	public Page<DatabaseRelease> searchReleases(String palabra, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
		return releaseRepository.findBySearchTerm(palabra, pageable);
	}

	@Timed
	public Page<DatabaseRelease> searchReleasesByArchived(String palabra, int page, int size, boolean archived) {
		PageRequest pageable = PageRequest.of(page, size);
		return releaseRepository.findByArchivedAndSearchTerm(palabra, archived, pageable);
	}

	@Timed
	public DatabaseRelease getRelease(Long id) {
		return releaseRepository.findById(id).get();
	}

	@Timed
	public void deleteReleases(List<Long> ids) {
		releaseRepository.deleteAllById(ids);
	}

	@Timed
	public void archiveReleases(List<Long> ids) {
		List<DatabaseRelease> releases = releaseRepository.findAllById(ids);
		for (DatabaseRelease release : releases) {
			release.setArchived(true);
		}
		releaseRepository.saveAll(releases);
	}

	@Timed
	public void unarchiveReleases(List<Long> ids) {
		List<DatabaseRelease> releases = releaseRepository.findAllById(ids);
		for (DatabaseRelease release : releases) {
			release.setArchived(false);
		}
		releaseRepository.saveAll(releases);
	}

	public boolean contains(Long id) {
		return releaseRepository.existsById(id);
	}

}
