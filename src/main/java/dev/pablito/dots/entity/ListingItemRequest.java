package dev.pablito.dots.entity;

public class ListingItemRequest {

	private String id;
	private String platform;
	private String link;
	private Double sellingPrice;

	public ListingItemRequest(String id, String platform, String link, Double sellingPrice) {
		super();
		this.id = id;
		this.platform = platform;
		this.link = link;
		this.sellingPrice = sellingPrice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
