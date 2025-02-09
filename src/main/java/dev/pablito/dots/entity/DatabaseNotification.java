package dev.pablito.dots.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseNotification {
	private String type;
	private String orderId;
	private String date;
	private boolean seen;
	
	
	
	public DatabaseNotification(String orderId, String type, String date) {
		super();
		this.type = type;
		this.orderId = orderId;
		this.date = date;
		seen = false;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	
	
}
