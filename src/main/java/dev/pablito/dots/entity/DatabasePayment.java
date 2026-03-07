package dev.pablito.dots.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Payments")
public class DatabasePayment {
	@Id
	private String id;
	private String orderId;
	private Double cost;
	private Double payout;
	private String creationDate;
	private String reason;

	public DatabasePayment() {
		super();
	}

	public DatabasePayment(Double cost, Double payout, String creationDate, String reason) {
		super();
		this.cost = cost;
		this.payout = payout;
		this.creationDate = creationDate;
		this.reason = reason;
	}

	public DatabasePayment(String orderId, Double cost, Double payout, String creationDate, String reason) {
		super();
		this.orderId = orderId;
		this.cost = cost;
		this.payout = payout;
		this.creationDate = creationDate;
		this.reason = reason;
	}

	public DatabasePayment(String id, String orderId, Double cost, Double payout, String creationDate, String reason) {
		super();
		this.id = id;
		this.orderId = orderId;
		this.cost = cost;
		this.payout = payout;
		this.creationDate = creationDate;
		this.reason = reason;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getPayout() {
		return payout;
	}

	public void setPayout(Double payout) {
		this.payout = payout;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
