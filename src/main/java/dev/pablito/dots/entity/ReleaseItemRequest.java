package dev.pablito.dots.entity;

import java.util.List;

public class ReleaseItemRequest {
	private Long id;
	private String name;
	private List<Artist> artists;
	private String thumb;

	public ReleaseItemRequest(Long id, String name, List<Artist> artists, String thumb) {
		super();
		this.id = id;
		this.name = name;
		this.artists = artists;
		this.thumb = thumb;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Artist> getArtists() {
		return artists;
	}

	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

}
