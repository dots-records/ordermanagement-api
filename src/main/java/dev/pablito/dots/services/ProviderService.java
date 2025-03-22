package dev.pablito.dots.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseProvider;
import dev.pablito.dots.repository.ListingRepository;
import dev.pablito.dots.repository.ProviderRepository;

@Service
public class ProviderService {
	
	@Autowired
	private ProviderRepository providerRepository;
	
	@Timed
	public void createProvider(long releaseId) throws IOException, InterruptedException {
		DatabaseProvider provider = new DatabaseProvider(releaseId, "Stock", 1.4, null, 1 );
		providerRepository.insert(provider);
	}
	
	@Timed
	public List<DatabaseProvider> getProviders(long releaseId) throws IOException, InterruptedException {
		return providerRepository.findByReleaseId(releaseId);
	}

}
