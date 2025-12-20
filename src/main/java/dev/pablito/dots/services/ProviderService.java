package dev.pablito.dots.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseProvider;
import dev.pablito.dots.entity.ProviderRequest;
import dev.pablito.dots.repository.ListingRepository;
import dev.pablito.dots.repository.ProviderRepository;

@Service
public class ProviderService {
	
	@Autowired
	private ProviderRepository providerRepository;
	
	@Timed
	public void createProvider(long releaseId, ProviderRequest request) throws IOException, InterruptedException {
		DatabaseProvider provider;
		if(request.getType().equals("In Stock")) {
			provider = new DatabaseProvider(releaseId, "In Stock", request.getPrice(), null,
					request.getUnits(), request.getCondition(), request.getDescription());
			providerRepository.insert(provider);
		} else if (request.getType().equals("Online")) {
			provider = new DatabaseProvider(releaseId, "Online", request.getPrice(), request.getLink(), 
					null, request.getCondition(), request.getDescription());
			providerRepository.insert(provider);
		}
		
	}
	
	@Timed
	public List<DatabaseProvider> getProviders(long releaseId) throws IOException, InterruptedException {
		return providerRepository.findByReleaseId(releaseId);
	}
	
	public void updateProvider(Long releaseId, String providerId, ProviderRequest request) {
		
	    DatabaseProvider provider = providerRepository
	        .findByIdAndReleaseId(providerId, releaseId)
	        .orElseThrow(() -> new RuntimeException("Provider not found"));
	       

	    provider.setType(request.getType());
	    provider.setPrice(request.getPrice());
	    provider.setCondition(request.getCondition());
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

	public void deleteProvider(Long releaseId, String providerId) {
	    DatabaseProvider provider = providerRepository
	        .findByIdAndReleaseId(providerId, releaseId)
	        .orElseThrow(() -> new RuntimeException("Provider not found"));

	    providerRepository.delete(provider);
	}


}
