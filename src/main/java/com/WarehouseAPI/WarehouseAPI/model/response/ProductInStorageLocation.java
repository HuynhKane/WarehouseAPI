package com.WarehouseAPI.WarehouseAPI.model.response;

import org.bson.types.ObjectId;

public class ProductInStorageLocation {
    private ObjectId storageLocationId;
    private Long totalInStock;

    public ObjectId getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(ObjectId storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public Long getTotalInStock() {
        return totalInStock;
    }

    public void setTotalInStock(Long totalInStock) {
        this.totalInStock = totalInStock;
    }
}
