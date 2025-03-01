package dev.pablito.dots.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseListing;
import dev.pablito.dots.entity.ListingRequest;
import dev.pablito.dots.repository.ListingRepository;

@Service
public class ListingService {

	@Autowired
	private ListingRepository listingRepository;
	
	@Timed
	public void createListing(long releaseId) throws IOException, InterruptedException {
		DatabaseListing listing = new DatabaseListing(releaseId, "Vinted", "hola.com");
		listingRepository.insert(listing);
	}
	
	@Timed
	public List<DatabaseListing> getListings(long releaseId) throws IOException, InterruptedException {
		return listingRepository.findByReleaseId(releaseId);
	}

}
