package dev.pablito.dots.entity;

import java.util.List;

import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem;

public class OrderRequest {

	private String platform;
	private List<DatabaseItem> items;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public List<DatabaseItem> getItems() {
		return items;
	}

	public void setItems(List<DatabaseItem> items) {
		this.items = items;
	}

}
