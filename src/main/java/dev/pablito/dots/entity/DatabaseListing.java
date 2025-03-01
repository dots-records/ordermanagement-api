package dev.pablito.dots.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Listings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseListing {
	
	private Long releaseId;
	private String type;
	private String link;
	
	
	public DatabaseListing(Long releaseId, String type, String link) {
		super();
		this.releaseId = releaseId;
		this.type = type;
		this.link = link;
	}
	
	public Long getReleaseId() {
		return releaseId;
	}
	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
	
}
