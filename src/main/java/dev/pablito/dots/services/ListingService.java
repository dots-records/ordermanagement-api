package dev.pablito.dots.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.entity.DatabaseListing;
import dev.pablito.dots.entity.DatabaseProvider;
import dev.pablito.dots.entity.ListingRequest;
import dev.pablito.dots.repository.ListingRepository;
import dev.pablito.dots.repository.ProviderRepository;

@Service
public class ListingService {

	@Autowired
	private ListingRepository listingRepository;
	@Autowired
	private DiscogsClient discogsClient;
	@Autowired
	private ProviderRepository providerRepository;
	private Double discogsRealBenefit = 0.85;

	@Timed
	public void createListing(long releaseId, String providerId, ListingRequest request)
			throws IOException, InterruptedException {
		DatabaseListing listing = new DatabaseListing(releaseId, providerId, request.getPlatform(), request.getLink(),
				request.getSellingPrice());
		if (listing.getPlatform().equals("Discogs")) {

			Optional<DatabaseProvider> optProvider = providerRepository.findById(providerId);
			if (optProvider.isEmpty()) {
				return;
			}

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
		listingRepository.insert(listing);
	}

	@Timed
	public void updateSellingPrice(long releaseId, String providerId, String listingId, Double newSellingPrice)
			throws IOException, InterruptedException {
		Optional<DatabaseListing> optListing = listingRepository.findByIdAndReleaseIdAndProviderId(listingId, releaseId,
				providerId);

		if (optListing.isEmpty()) {
			return;
		}
		DatabaseListing listing = optListing.get();
		if (listing.getPlatform().equals("Discogs") && listing.getDiscogsListingId() != null) {
			double priceWithFees = Math.round((newSellingPrice / discogsRealBenefit) * 10.0) / 10.0;
			String response = discogsClient.updateDiscogsListingSellingPrice(listing.getDiscogsListingId(),
					priceWithFees);
		}
		listing.setSellingPrice(newSellingPrice);
		listing.setDateLastEdition(LocalDateTime.now());
		listingRepository.save(listing);
	}

	@Timed
	public void updateLink(long releaseId, String providerId, String listingId, String newLink) {
		Optional<DatabaseListing> optListing = listingRepository.findByIdAndReleaseIdAndProviderId(listingId, releaseId,
				providerId);

		if (optListing.isEmpty()) {
			return;
		}
		DatabaseListing listing = optListing.get();
		listing.setLink(newLink);
		listing.setDateLastEdition(LocalDateTime.now());
		listingRepository.save(listing);
	}

	@Timed
	public List<DatabaseListing> getListings(long releaseId, String providerId)
			throws IOException, InterruptedException {
		return listingRepository.findByReleaseIdAndProviderId(releaseId, providerId);
	}

	@Timed
	public void deleteListing(long releaseId, String providerId, String listingId)
			throws IOException, InterruptedException {

		Optional<DatabaseListing> optListing = listingRepository.findByIdAndReleaseIdAndProviderId(listingId, releaseId,
				providerId);

		if (optListing.isEmpty()) {
			return;
		}

		DatabaseListing listing = optListing.get();
		if ("Discogs".equals(listing.getPlatform()) && listing.getDiscogsListingId() != null) {
			discogsClient.deleteDiscogsListing(listing.getDiscogsListingId());
		}
		listingRepository.delete(listing);
	}

}
