package dev.pablito.dots.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Listings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseListing {
	@Id
	private String id;
	private Long releaseId;
	private String providerId;
	private String platform;
	private String link;
	private Double sellingPrice;

	public DatabaseListing(Long releaseId, String providerId, String platform, String link, Double sellingPrice) {
		super();
		this.providerId = providerId;
		this.releaseId = releaseId;
		this.platform = platform;
		this.link = link;
		this.sellingPrice = sellingPrice;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public Long getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

}
