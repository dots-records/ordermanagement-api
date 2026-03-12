package dev.pablito.dots.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseProvider;
import dev.pablito.dots.entity.ProviderRequest;
import dev.pablito.dots.exceptions.InvalidException;
import dev.pablito.dots.exceptions.MongoException;
import dev.pablito.dots.exceptions.NotFoundException;
import dev.pablito.dots.repository.ProviderRepository;
import dev.pablito.dots.repository.ReleaseRepository;

@Service
public class ProviderService {

	@Autowired
	private ProviderRepository providerRepository;
	@Autowired
	private ReleaseRepository releaseRepository;

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void createProvider(long releaseId, ProviderRequest request)
			throws IOException, InterruptedException, InvalidException, MongoException, NotFoundException {
		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this provider");
		}
		DatabaseProvider provider;
		if (request.getType().equals("In Stock")) {
			provider = new DatabaseProvider(releaseId, "In Stock", request.getPrice(), null, request.getUnits(),
					request.getDiscCondition(), request.getSleeveCondition(), request.getDescription());
			try {
				providerRepository.insert(provider);
			} catch (Exception e) {
				throw new MongoException("Error creating provider in MongoDB: " + e.getMessage());
			}
		} else if (request.getType().equals("Online")) {
			provider = new DatabaseProvider(releaseId, "Online", request.getPrice(), request.getLink(), null,
					request.getDiscCondition(), request.getSleeveCondition(), request.getDescription());
			try {
				providerRepository.insert(provider);
			} catch (Exception e) {
				throw new MongoException("Error creating provider in MongoDB: " + e.getMessage());
			}
		} else {
			throw new InvalidException("Invalid provider type");
		}

	}

	@Timed
	public List<DatabaseProvider> getProviders(long releaseId) throws IOException, InterruptedException {
		return providerRepository.findByReleaseId(releaseId);
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateProvider(Long releaseId, String providerId, ProviderRequest request)
			throws NotFoundException, InvalidException, MongoException {
		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this provider");
		}

		Optional<DatabaseProvider> optProvider = providerRepository.findByIdAndReleaseId(providerId, releaseId);
		if (optProvider.isEmpty()) {
			throw new NotFoundException("Problem finding this provider");
		}
		DatabaseProvider provider = optProvider.get();
		if ("In Stock".equals(request.getType())) {
			provider.setUnits(request.getUnits());
			provider.setLink(null);
		} else if ("Online".equals(request.getType())) {
			provider.setLink(request.getLink());
			provider.setUnits(null);
		} else {
			throw new InvalidException("Invalid provider type");
		}

		provider.setType(request.getType());
		provider.setPrice(request.getPrice());
		provider.setDiscCondition(request.getDiscCondition());
		provider.setSleeveCondition(request.getSleeveCondition());
		provider.setDescription(request.getDescription());

		try {
			providerRepository.save(provider);
		} catch (Exception e) {
			throw new MongoException("Error updating provider in MongoDB: " + e.getMessage());
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void updateProviderUnits(Long releaseId, String providerId, Integer units)
			throws NotFoundException, MongoException, InvalidException {
		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this provider");
		}
		Optional<DatabaseProvider> optProvider = providerRepository.findByIdAndReleaseId(providerId, releaseId);
		if (optProvider.isEmpty()) {
			throw new NotFoundException("Problem finding this provider");
		}
		DatabaseProvider provider = optProvider.get();

		if ("In Stock".equals(provider.getType())) {
			provider.setUnits(units);
		} else {
			throw new InvalidException("Provider type is not Stock");
		}
		try {
			providerRepository.save(provider);
		} catch (Exception e) {
			throw new MongoException("Error updating provider's units in MongoDB: " + e.getMessage());
		}

	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void deleteProvider(Long releaseId, String providerId) throws NotFoundException, MongoException {

		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this provider");
		}

		Optional<DatabaseProvider> optProvider = providerRepository.findByIdAndReleaseId(providerId, releaseId);
		if (optProvider.isEmpty()) {
			throw new NotFoundException("Problem finding this provider");
		}
		try {
			providerRepository.delete(optProvider.get());
		} catch (Exception e) {
			throw new MongoException("Error deleting provider in MongoDB: " + e.getMessage());
		}

	}

	@Timed
	public Boolean existsProvider(long releaseId, String providerId)
			throws IOException, InterruptedException, NotFoundException {
		if (!releaseRepository.existsById(releaseId)) {
			throw new NotFoundException("Problem finding the release associated with this provider");
		}
		Optional<DatabaseProvider> optProvider = providerRepository.findByIdAndReleaseId(providerId, releaseId);

		return optProvider.isPresent();
	}

}
