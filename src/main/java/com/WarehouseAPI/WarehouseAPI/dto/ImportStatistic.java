package com.WarehouseAPI.WarehouseAPI.dto;

import java.time.LocalDateTime;

public class ImportStatistic {

    private LocalDateTime importDate;
    private int totalProducts;

    // Constructors
    public ImportStatistic(LocalDateTime importDate, int totalProducts) {
        this.importDate = importDate;
        this.totalProducts = totalProducts;
    }

    // Getters and Setters
    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }
}
