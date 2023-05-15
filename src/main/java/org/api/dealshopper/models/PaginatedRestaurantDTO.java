package org.api.dealshopper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedRestaurantDTO {
    private List<RestaurantDTO> content;
    private int currentPageNumber;
    private int pageSize;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public List<RestaurantDTO> getContent() {
        return content;
    }

    public void setContent(List<RestaurantDTO> content) {
        this.content = content;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public PaginatedRestaurantDTO(List<RestaurantDTO> content, int pageNumber, int pageSize, int totalPages) {
        this.content = content;
        this.currentPageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.hasPrevious = pageNumber > 1;
        this.hasNext = pageNumber < totalPages;
    }

}
