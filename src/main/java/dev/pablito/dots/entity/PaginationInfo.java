package dev.pablito.dots.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class PaginationInfo {
    private int page;
    private int pages;
    private int items;
    private int per_page;
    private PaginationUrls urls;


    public static class PaginationUrls {
		private String next;

		public String getNext() {
			return next;
		}

		public void setNext(String next) {
			this.next = next;
		}

		@Override
		public String toString() {
			return "Payment [next=" + next + "]";
		}
		
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getPages() {
		return pages;
	}


	public void setPages(int pages) {
		this.pages = pages;
	}


	public int getItems() {
		return items;
	}


	public void setItems(int items) {
		this.items = items;
	}


	public int getPer_page() {
		return per_page;
	}


	public void setPer_page(int per_page) {
		this.per_page = per_page;
	}


	public PaginationUrls getUrls() {
		return urls;
	}


	public void setUrls(PaginationUrls urls) {
		this.urls = urls;
	}


	@Override
	public String toString() {
		return "PaginationInfo [page=" + page + ", pages=" + pages + ", items=" + items + ", per_page=" + per_page
				+ ", urls=" + urls + "]";
	}
    
    
    
    
	
	
    
    
}
