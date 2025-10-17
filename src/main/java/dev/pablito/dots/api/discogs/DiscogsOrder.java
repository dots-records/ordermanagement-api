package dev.pablito.dots.api.discogs;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class DiscogsOrder {
	@Id
	private String id;
	private List<String> next_status;
	private String created;
	private String status;
	private Shipping shipping;
	private boolean archived;
	private Price total;
	private String shipping_address;
	private Double calculated_fee;
	private Double revenue;
	private List<Item> items;
	private String uri;
	
	public static class Price {
		private String currency;
        private double value;
        
		@Override
		public String toString() {
			return "Fee [currency=" + currency + ", value=" + value + "]";
		}
		
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public double getValue() {
			return value;
		}
		public void setValue(double value) {
			this.value = value;
		}
	}
	
	public static class Shipping {
		private String currency;
		private String method;
		private double value;
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
		public double getValue() {
			return value;
		}
		public void setValue(double value) {
			this.value = value;
		}
		
	}
	
	public static class Item {
		private Release release;
		private Price price;
		private String media_condition;
		private String sleeve_condition;
		private Long id;
		public static class Release {
			private Long id;
			private String description;
			public Long getId() {
				return id;
			}
			public void setId(Long id) {
				this.id = id;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
		}
		
		public static class Price {
			private String currency;
			private Integer value;
			public String getCurrency() {
				return currency;
			}
			public void setCurrency(String currency) {
				this.currency = currency;
			}
			public Integer getValue() {
				return value;
			}
			public void setValue(Integer value) {
				this.value = value;
			}
		}
		public Release getRelease() {
			return release;
		}
		public void setRelease(Release release) {
			this.release = release;
		}
		
		public Price getPrice() {
			return price;
		}
		public void setPrice(Price price) {
			this.price = price;
		}
		public String getMedia_condition() {
			return media_condition;
		}
		public void setMedia_condition(String media_condition) {
			this.media_condition = media_condition;
		}
		public String getSleeve_condition() {
			return sleeve_condition;
		}
		public void setSleeve_condition(String sleeve_condition) {
			this.sleeve_condition = sleeve_condition;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getNext_status() {
		return next_status;
	}
	public void setNext_status(List<String> next_status) {
		this.next_status = next_status;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	
	public Shipping getShipping() {
		return shipping;
	}
	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}
	
	public boolean isArchived() {
		return archived;
	}
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	
	public Price getTotal() {
		return total;
	}
	public void setTotal(Price total) {
		this.total = total;
	}
	
	public String getShipping_address() {
		return shipping_address;
	}
	public void setShipping_address(String shipping_address) {
		this.shipping_address = shipping_address;
	}
	
	public Double getCalculated_fee() {
		return calculated_fee;
	}
	public void setCalculated_fee(Double calculated_fee) {
		this.calculated_fee = calculated_fee;
	}
	
	public Double getRevenue() {
		return revenue;
	}
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}
	
	
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", status=" + status + ", next_status=" + next_status + ", created=" + created + "]";
	}
	
	
}
