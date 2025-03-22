package dev.pablito.dots.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Providers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseProvider {
	private Long releaseId;
	private String type;
	private Double price;
	private String link;
	private Integer units;
	
	
	public DatabaseProvider(Long releaseId, String type, Double price, String link, Integer units) {
		super();
		this.releaseId = releaseId;
		this.type = type;
		this.price = price;
		this.link = link;
		this.units = units;
	}
	
	public Long getReleaseId() {
		return releaseId;
	}
	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
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
	
	
	
	

}
