package com.WarehouseAPI.WarehouseAPI.model.response;

import org.bson.types.ObjectId;

public class ProductInStorageLocation {
    private String storageLocationId;
    private Long totalInStock;

    public String getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(String storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public Long getTotalInStock() {
        return totalInStock;
    }

    public void setTotalInStock(Long totalInStock) {
        this.totalInStock = totalInStock;
    }
}
