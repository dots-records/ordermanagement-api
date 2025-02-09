package dev.pablito.dots.api.discogs;

import java.util.List;

import dev.pablito.dots.entity.PaginationInfo;

public class DiscogsApiResponse {
    private PaginationInfo pagination;
    private List<DiscogsOrder> orders;  // Cambiado de ListDiscogsOrders a List<DiscogsOrder>

    // Getters y Setters
    public PaginationInfo getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }

    public List<DiscogsOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DiscogsOrder> orders) {
        this.orders = orders;
    }
}
