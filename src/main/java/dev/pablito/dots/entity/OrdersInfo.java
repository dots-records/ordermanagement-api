package dev.pablito.dots.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "ordersInformation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersInfo {
	@Id
	private String id;
	private String createdAfter;
	
	public OrdersInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedAfter() {
		return createdAfter;
	}

	public void setCreatedAfter(String createdAfter) {
		this.createdAfter = createdAfter;
	}
	
	
	

}
