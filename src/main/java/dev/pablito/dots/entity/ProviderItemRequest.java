package dev.pablito.dots.entity;

public class ProviderItemRequest {
	private String id;
	private String type;
	private Double price;
	private String link;
	private Integer units;
	private String discCondition;
	private String sleeveCondition;
	private String description;

	public ProviderItemRequest(String id, String type, Double price, String link, Integer units, String discCondition,
			String sleeveCondition, String description) {
		super();
		this.id = id;
		this.type = type;
		this.price = price;
		this.link = link;
		this.units = units;
		this.discCondition = discCondition;
		this.sleeveCondition = sleeveCondition;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public String getDiscCondition() {
		return discCondition;
	}

	public void setDiscCondition(String discCondition) {
		this.discCondition = discCondition;
	}

	public String getSleeveCondition() {
		return sleeveCondition;
	}

	public void setSleeveCondition(String sleeveCondition) {
		this.sleeveCondition = sleeveCondition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
