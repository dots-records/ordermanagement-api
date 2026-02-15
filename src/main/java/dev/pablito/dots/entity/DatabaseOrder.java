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
	private String platform;
	private List<DatabaseItem> items;
	private String status;
	private String created;
	private String createdComplete;
	private boolean archived;
	private Payment payment;
	private Double revenue;
	private boolean notified;
	private MessageManager messageManager;
	private Integer newMessagesCustomer;
	private Integer newMessagesSeller;
	private Integer newMessagesDiscogs;
	private boolean justAdded;
	private boolean associated;
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
		@Id
		private Long id;
		private DatabaseReleaseItem release;
		private DatabaseProviderItem provider;
		private DatabaseListingItem listing;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public DatabaseReleaseItem getRelease() {
			return release;
		}

		public void setRelease(DatabaseReleaseItem release) {
			this.release = release;
		}

		public DatabaseProviderItem getProvider() {
			return provider;
		}

		public void setProvider(DatabaseProviderItem provider) {
			this.provider = provider;
		}

		public DatabaseListingItem getListing() {
			return listing;
		}

		public void setListing(DatabaseListingItem listing) {
			this.listing = listing;
		}

		public static class DatabaseReleaseItem {
			private Long id;
			private String name;
			private List<Artist> artists;
			private String thumb;

			public DatabaseReleaseItem(Long id, String name, List<Artist> artists, String thumb) {
				super();
				this.id = id;
				this.name = name;
				this.artists = artists;
				this.thumb = thumb;
			}

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

		public static class DatabaseProviderItem {
			private String id;
			private String type;
			private Double price;
			private String link;
			private Integer units;
			private String discCondition;
			private String sleeveCondition;
			private String description;

			public DatabaseProviderItem(String id, String type, Double price, String link, Integer units,
					String discCondition, String sleeveCondition, String description) {
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

		public static class DatabaseListingItem {
			private String id;
			private String platform;
			private String link;
			private Double sellingPrice;

			public DatabaseListingItem(String id, String platform, String link, Double sellingPrice) {
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
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

	public boolean isJustAdded() {
		return justAdded;
	}

	public void setJustAdded(boolean justAdded) {
		this.justAdded = justAdded;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isAssociated() {
		return associated;
	}

	public void setAssociated(boolean associated) {
		this.associated = associated;
	}

}
