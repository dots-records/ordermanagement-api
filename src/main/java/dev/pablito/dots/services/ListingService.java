package dev.pablito.dots.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.entity.DatabaseListing;
import dev.pablito.dots.entity.DatabaseProvider;
import dev.pablito.dots.entity.ListingRequest;
import dev.pablito.dots.exceptions.DiscogsException;
import dev.pablito.dots.exceptions.MongoException;
import dev.pablito.dots.exceptions.NotFoundException;
import dev.pablito.dots.repository.ListingRepository;
import dev.pablito.dots.repository.ProviderRepository;
import dev.pablito.dots.repository.ReleaseRepository;

@Service
public class ListingService {

	@Autowired
	private ListingRepository listingRepository;
	@Autowired
	private DiscogsClient discogsClient;
	@Autowired
	private ProviderRepository providerRepository;
	@Autowired
	private ReleaseRepository releaseRepository;
	private Double discogsRealBenefit = 0.85;

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void createListing(long releaseId, String providerId, ListingRequest request)
			throws MongoException, DiscogsException, IOException, InterruptedException, NotFoundException {
		DatabaseListing listing = new DatabaseListing(releaseId, providerId, request.getPlatform(), request.getLink(),
				request.getSellingPrice());
		if (!providerRepository.existsById(providerId)) {
			throw new NotFoundException("Problem finding the provider associated with this listing");
		}
		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this listing");
		}
		if (listing.getPlatform().equals("Discogs")) {
			Optional<DatabaseProvider> optProvider = providerRepository.findById(providerId);
			DatabaseProvider provider = optProvider.get();

			double sellingPrice = listing.getSellingPrice();
			double priceWithFees = Math.round((sellingPrice / discogsRealBenefit) * 10.0) / 10.0;
			String response = discogsClient.createListing(releaseId, priceWithFees, provider.getDiscCondition(),
					provider.getSleeveCondition(), provider.getDescription());
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(response);

			Long discogsListingId = node.get("listing_id").asLong();
			listing.setDiscogsListingId(discogsListingId);
			listing.setLink("https://www.discogs.com/es/sell/item/" + discogsListingId);

		}
		listing.setDateLastEdition(LocalDateTime.now());
		try {
			listingRepository.insert(listing);
		} catch (Exception e) {
			if (listing.getPlatform().equals("Discogs")) {
				discogsClient.deleteDiscogsListing(listing.getDiscogsListingId());
			}
			throw new MongoException("Error saving listing in MongoDB: " + e.getMessage());
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateSellingPrice(long releaseId, String providerId, String listingId, Double newSellingPrice)
			throws IOException, InterruptedException, NotFoundException, MongoException, DiscogsException {
		if (!providerRepository.existsById(providerId)) {
			throw new NotFoundException("Problem finding the provider associated with this listing");
		}
		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this listing");
		}
		Optional<DatabaseListing> optListing = listingRepository.findByIdAndReleaseIdAndProviderId(listingId, releaseId,
				providerId);

		if (optListing.isEmpty()) {
			throw new NotFoundException("Problem finding this listing");
		}
		DatabaseListing listing = optListing.get();
		listing.setSellingPrice(newSellingPrice);
		listing.setDateLastEdition(LocalDateTime.now());
		try {
			listingRepository.save(listing);
		} catch (Exception e) {
			throw new MongoException("Error saving selling price of this listing in MongoDB: " + e.getMessage());
		}
		if (listing.getPlatform().equals("Discogs") && listing.getDiscogsListingId() != null) {
			double priceWithFees = Math.round((newSellingPrice / discogsRealBenefit) * 10.0) / 10.0;
			discogsClient.updateDiscogsListingSellingPrice(listing.getDiscogsListingId(), priceWithFees);
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateLink(long releaseId, String providerId, String listingId, String newLink)
			throws NotFoundException, MongoException {
		if (!providerRepository.existsById(providerId)) {
			throw new NotFoundException("Problem finding the provider associated with this listing");
		}
		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this listing");
		}
		Optional<DatabaseListing> optListing = listingRepository.findByIdAndReleaseIdAndProviderId(listingId, releaseId,
				providerId);

		if (optListing.isEmpty()) {
			throw new NotFoundException("Problem finding this listing");
		}
		DatabaseListing listing = optListing.get();
		listing.setLink(newLink);
		listing.setDateLastEdition(LocalDateTime.now());
		try {
			listingRepository.save(listing);
		} catch (Exception e) {
			throw new MongoException("Error saving link of this listing in MongoDB: " + e.getMessage());
		}
	}

	@Timed
	public List<DatabaseListing> getListings(long releaseId, String providerId)
			throws IOException, InterruptedException {
		return listingRepository.findByReleaseIdAndProviderId(releaseId, providerId);
	}

	@Timed
	public Boolean existsListing(long releaseId, String providerId, String listingId)
			throws IOException, InterruptedException, NotFoundException {
		if (!providerRepository.existsById(providerId)) {
			throw new NotFoundException("Problem finding the provider associated with this listing");
		}
		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this listing");
		}
		Optional<DatabaseListing> optListing = listingRepository.findByIdAndReleaseIdAndProviderId(listingId, releaseId,
				providerId);

		return optListing.isPresent();
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void deleteListing(long releaseId, String providerId, String listingId)
			throws IOException, InterruptedException, DiscogsException, NotFoundException, MongoException {

		if (!providerRepository.existsById(providerId)) {
			throw new NotFoundException("Problem finding the provider associated with this listing");
		}
		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this listing");
		}
		Optional<DatabaseListing> optListing = listingRepository.findByIdAndReleaseIdAndProviderId(listingId, releaseId,
				providerId);

		if (optListing.isEmpty()) {
			throw new NotFoundException("Problem finding this listing");
		}
		DatabaseListing listing = optListing.get();

		try {
			listingRepository.delete(listing);
		} catch (Exception e) {
			throw new MongoException("Error deleting listing in MongoDB: " + e.getMessage());
		}

		if ("Discogs".equals(listing.getPlatform()) && listing.getDiscogsListingId() != null) {
			discogsClient.deleteDiscogsListing(listing.getDiscogsListingId());
		}
	}

}
