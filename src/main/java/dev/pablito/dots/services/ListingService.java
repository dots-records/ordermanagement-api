package dev.pablito.dots.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.controller.ProviderController;
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
	// TODO: Quitar
	private static final Logger logger = LoggerFactory.getLogger(ProviderController.class);

	@Timed
	public void createListing(long releaseId, String providerId, ListingRequest request)
			throws IOException, InterruptedException {
		DatabaseListing listing = new DatabaseListing(releaseId, providerId, request.getPlatform(), request.getLink(),
				request.getSellingPrice());

		// TODO: Activar el if
		// if (listing.getPlatform() == "Discogs") {

		Optional<DatabaseProvider> optProvider = providerRepository.findById(providerId);
		if (optProvider.isEmpty()) {
			return;
		}

		DatabaseProvider provider = optProvider.get();
		String response = discogsClient.createListing(releaseId, request.getSellingPrice(), provider.getDiscCondition(),
				provider.getSleeveCondition(), provider.getDescription());
		// TODO: Extraer link y guardar link en el listing. Ver que hacer con el discogs
		// listingid, lo
		// necesito para editar y borrar
		logger.info(response);
		// }
		listingRepository.insert(listing);
	}

	@Timed
	public List<DatabaseListing> getListings(long releaseId, String providerId)
			throws IOException, InterruptedException {
		return listingRepository.findByReleaseIdAndProviderId(releaseId, providerId);
	}

}
