package dev.pablito.dots.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

	public Page<DatabaseRelease> getAllReleases(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size);
		Page<DatabaseRelease> releasePage = releaseRepository.findAll(pageable);
		return releasePage;
	}
	
	public Page<DatabaseRelease> getArchivedReleases(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size);
		Page<DatabaseRelease> releasePage = releaseRepository.findByArchived(true, pageable);
		return releasePage;
	}
	
	public Page<DatabaseRelease> getUnarchivedReleases(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size);
		Page<DatabaseRelease> releasePage = releaseRepository.findByArchived(false, pageable);
		return releasePage;
	}

	public Page<DatabaseRelease> searchReleases(String palabra, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
		return releaseRepository.findBySearchTerm(palabra, pageable);
	}
	
	public Page<DatabaseRelease> searchReleasesByArchived(String palabra, int page, int size, boolean archived) {
		PageRequest pageable = PageRequest.of(page, size);
		return releaseRepository.findByArchivedAndSearchTerm(archived, palabra, pageable);
	}
	
	public DatabaseRelease getRelease(Long id) throws IOException, InterruptedException {
		System.out.println(id);
		DatabaseRelease dbRel =  releaseRepository.findById(id).get();
		System.out.println(dbRel);
		return releaseRepository.findById(id).get();
	}
	
	

}
