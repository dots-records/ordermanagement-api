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
	private Double price;
	
	
	public DatabaseListing(Long releaseId, String type, String link, Double price) {
		super();
		this.releaseId = releaseId;
		this.type = type;
		this.link = link;
		this.price = price;	}
	
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
}
