package dev.pablito.dots.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseOrder {
	@Id
	private String id;
	private String discogsId;
	private String type;
	private List<DatabaseItem> items;
	private String status;
	private String created;
	private String createdComplete;
	private boolean archived;
	private Payment payment;
	private Double revenue;
	private Provider provider;
	private boolean notified;
	private MessageManager messageManager;
	private Integer newMessagesCustomer;
	private Integer newMessagesSeller;
	private Integer newMessagesDiscogs;
	private boolean changed;
	private String uri;
	
	
	public static class Payment {
		private Double shipping;
		private Double items;
		public Double getShipping() {
			return shipping;
		}
		public void setShipping(Double shipping) {
			this.shipping = shipping;
		}
		public Double getItems() {
			return items;
		}
		public void setItems(Double items) {
			this.items = items;
		}
	}
	
	public static class DatabaseItem {
		private Long id;
		private String name;
		private List<Artist> artists;
		private String thumb;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Artist> getArtists() {
			return artists;
		}
		public void setArtists(List<Artist> artists) {
			this.artists = artists;
		}
		public String getThumb() {
			return thumb;
		}

		public void setThumb(String thumb) {
			this.thumb = thumb;
		}
	}
	
	public static class Provider {
		private String name;
		private String information;
		private Integer price;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getInformation() {
			return information;
		}
		public void setInformation(String information) {
			this.information = information;
		}
		public Integer getPrice() {
			return price;
		}
		public void setPrice(Integer price) {
			this.price = price;
		}
	}

	public String getDiscogsId() {
		return discogsId;
	}

	public void setDiscogsId(String discogsId) {
		this.discogsId = discogsId;
	}

	public List<DatabaseItem> getItems() {
		return items;
	}

	public void setItems(List<DatabaseItem> items) {
		this.items = items;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isNotified() {
		return notified;
	}

	public void setNotified(boolean notified) {
		this.notified = notified;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getNewMessagesCustomer() {
		return newMessagesCustomer;
	}

	public void setNewMessagesCustomer(Integer newMessagesCustomer) {
		this.newMessagesCustomer = newMessagesCustomer;
	}

	public Integer getNewMessagesSeller() {
		return newMessagesSeller;
	}

	public void setNewMessagesSeller(Integer newMessagesSeller) {
		this.newMessagesSeller = newMessagesSeller;
	}

	public Integer getNewMessagesDiscogs() {
		return newMessagesDiscogs;
	}

	public void setNewMessagesDiscogs(Integer newMessagesDiscogs) {
		this.newMessagesDiscogs = newMessagesDiscogs;
	}

	public String getCreatedComplete() {
		return createdComplete;
	}

	public void setCreatedComplete(String createdComplete) {
		this.createdComplete = createdComplete;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	
	
	
	
	
	
}
