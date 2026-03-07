package dev.pablito.dots.mapper;

import java.io.IOException;
import java.time.LocalDateTime;

import dev.pablito.dots.api.discogs.DiscogsRelease;
import dev.pablito.dots.entity.DatabaseRelease;

public class ReleaseMapper {

	public DatabaseRelease mapToDatabaseRelease(DiscogsRelease release) throws IOException, InterruptedException {
		if (release == null) {
			return null;
		}

		DatabaseRelease dbRelease = new DatabaseRelease();
		dbRelease.setId(release.getId());
		dbRelease.setTitle(release.getTitle());
		dbRelease.setThumb(release.getThumb());
		dbRelease.setArtists(release.getArtists());
		dbRelease.setFormats(release.getFormats());
		dbRelease.setArchived(false);
		dbRelease.setImages(release.getImages());
		dbRelease.setDateLastEdition(LocalDateTime.now());
		dbRelease.setUri(release.getUri());
		return dbRelease;
	}

}
