package dev.pablito.dots.api.discogs;

import java.util.List;

import org.springframework.data.annotation.Id;

import dev.pablito.dots.entity.Artist;
import dev.pablito.dots.entity.Format;

public class DiscogsRelease {
	@Id
	private Long id;
	private String title;
	private String thumb;
	private List<Artist> artists;
	private List<Format> formats;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Artist> getArtists() {
		return artists;
	}

	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}

	public List<Format> getFormats() {
		return formats;
	}

	public void setFormats(List<Format> formats) {
		this.formats = formats;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	
}
