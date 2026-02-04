package dev.pablito.dots.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseListing;
import dev.pablito.dots.entity.ListingRequest;
import dev.pablito.dots.repository.ListingRepository;

@Service
public class ListingService {

	@Autowired
	private ListingRepository listingRepository;

	@Timed
	public void createListing(long releaseId, String providerId, ListingRequest request)
			throws IOException, InterruptedException {
		DatabaseListing listing = new DatabaseListing(releaseId, providerId, request.getPlatform(), request.getLink(),
				request.getSellingPrice());
		listingRepository.insert(listing);
	}

	@Timed
	public List<DatabaseListing> getListings(long releaseId, String providerId)
			throws IOException, InterruptedException {
		return listingRepository.findByReleaseIdAndProviderId(releaseId, providerId);
	}

}
