package dev.pablito.dots.services;

import org.springframework.beans.factory.annotation.Autowired;

import dev.pablito.dots.repository.ListingRepository;

public class ListingService {

	@Autowired
	private ListingRepository listingRepository;

}
