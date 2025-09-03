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
		if(request.getType().equals("Stock")) {
			provider = new DatabaseProvider(releaseId, "Stock", request.getPrice(), null, request.getUnits(), request.getCondition());
			providerRepository.insert(provider);
		} else if (request.getType().equals("Online")) {
			provider = new DatabaseProvider(releaseId, "Online", request.getPrice(), request.getLink(), null, null);
			providerRepository.insert(provider);
		}
		
	}
	
	@Timed
	public List<DatabaseProvider> getProviders(long releaseId) throws IOException, InterruptedException {
		return providerRepository.findByReleaseId(releaseId);
	}

}
