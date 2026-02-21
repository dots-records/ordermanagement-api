package dev.pablito.dots.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseProvider;
import dev.pablito.dots.entity.ProviderRequest;
import dev.pablito.dots.repository.ProviderRepository;

@Service
public class ProviderService {

	@Autowired
	private ProviderRepository providerRepository;

	@Timed
	public void createProvider(long releaseId, ProviderRequest request) throws IOException, InterruptedException {
		DatabaseProvider provider;
		if (request.getType().equals("In Stock")) {
			provider = new DatabaseProvider(releaseId, "In Stock", request.getPrice(), null, request.getUnits(),
					request.getDiscCondition(), request.getSleeveCondition(), request.getDescription());
			providerRepository.insert(provider);
		} else if (request.getType().equals("Online")) {
			provider = new DatabaseProvider(releaseId, "Online", request.getPrice(), request.getLink(), null,
					request.getDiscCondition(), request.getSleeveCondition(), request.getDescription());
			providerRepository.insert(provider);
		}

	}

	@Timed
	public List<DatabaseProvider> getProviders(long releaseId) throws IOException, InterruptedException {
		return providerRepository.findByReleaseId(releaseId);
	}

	public void updateProvider(Long releaseId, String providerId, ProviderRequest request) {

		DatabaseProvider provider = providerRepository.findByIdAndReleaseId(providerId, releaseId)
				.orElseThrow(() -> new RuntimeException("Provider not found"));

		provider.setType(request.getType());
		provider.setPrice(request.getPrice());
		provider.setDiscCondition(request.getDiscCondition());
		provider.setSleeveCondition(request.getSleeveCondition());
		provider.setDescription(request.getDescription());

		if ("In Stock".equals(request.getType())) {
			provider.setUnits(request.getUnits());
			provider.setLink(null);
		} else {
			provider.setLink(request.getLink());
			provider.setUnits(null);
		}

		providerRepository.save(provider);
	}

	public void updateProviderUnits(Long releaseId, String providerId, Integer units) {

		DatabaseProvider provider = providerRepository.findByIdAndReleaseId(providerId, releaseId)
				.orElseThrow(() -> new RuntimeException("Provider not found"));
		if ("In Stock".equals(provider.getType())) {
			provider.setUnits(units);
		}
		providerRepository.save(provider);
	}

	public void deleteProvider(Long releaseId, String providerId) {
		DatabaseProvider provider = providerRepository.findByIdAndReleaseId(providerId, releaseId)
				.orElseThrow(() -> new RuntimeException("Provider not found"));

		providerRepository.delete(provider);
	}

}
