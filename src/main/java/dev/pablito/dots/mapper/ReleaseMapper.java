package dev.pablito.dots.mapper;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
        return dbRelease;
    }

}
