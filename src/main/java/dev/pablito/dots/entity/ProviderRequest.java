package dev.pablito.dots.entity;

public class ProviderRequest {
	private String type;
	private double price;
	private String link;
	private int units;
	private String discCondition;
	private String sleeveCondition;
	private String description;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
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

	@Override
	public String toString() {
		return "ProviderRequest [type=" + type + ", price=" + price + ", link=" + link + ", units=" + units
				+ ", discCondition=" + discCondition + ", sleeveCondition=" + sleeveCondition + ", description="
				+ description + "]";
	}

}
