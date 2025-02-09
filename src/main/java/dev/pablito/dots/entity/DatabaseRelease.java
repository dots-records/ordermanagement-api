package dev.pablito.dots.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Releases")
public class DatabaseRelease {
	@Id
	private Long id;
	private String title;
	private String thumb;
	private List<Artist> artists;
	private List<Format> formats;
	
	private static class Format {
		private List<String> descriptions;
		private String name;
		
		public List<String> getDescriptions() {
			return descriptions;
		}
		public void setDescriptions(List<String> descriptions) {
			this.descriptions = descriptions;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		
	}

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
