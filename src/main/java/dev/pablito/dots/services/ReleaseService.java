package dev.pablito.dots.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.api.discogs.DiscogsRelease;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.DatabaseRelease;
import dev.pablito.dots.mapper.ReleaseMapper;
import dev.pablito.dots.repository.ReleaseRepository;

@Service
public class ReleaseService {
	@Autowired
	private ReleaseRepository releaseRepository;
	@Autowired
	private DiscogsClient discogsClient;

	public void putReleaseFromDiscogs(Long id) throws IOException, InterruptedException {
		DiscogsRelease dsRelease = discogsClient.getRelease(id);
		ReleaseMapper mapper = new ReleaseMapper();
		DatabaseRelease dbRelease = mapper.mapToDatabaseRelease(dsRelease);
		releaseRepository.insert(dbRelease);
	}

	public Page<DatabaseRelease> getReleases(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size);
		Page<DatabaseRelease> releasePage = releaseRepository.findAll(pageable);
		return releasePage;
	}

	public Page<DatabaseRelease> searchReleases(String palabra, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
		return releaseRepository.findBySearchTerm(palabra, pageable);
	}

}
