package com.WarehouseAPI.WarehouseAPI.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "product")
public class Product {
    @Id
    private String _id;
    private String productName;
    @Field("genreId")
    private ObjectId genreId;
    private int quantity;
    private String description;
    private BigDecimal importPrice;
    private BigDecimal sellingPrice;
    @Field("supplierId")
    private ObjectId supplierId;
    private boolean isInStock;
    @Field("storageLocationId")
    private ObjectId storageLocationId;
    private LocalDateTime lastUpdated;
    private String image;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ObjectId getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(ObjectId supplierId) {
        this.supplierId = supplierId;
    }

    public ObjectId getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(ObjectId storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ObjectId getGenreId() {
        return genreId;
    }

    public void setGenreId(ObjectId genreId) {
        this.genreId = genreId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(BigDecimal importPrice) {
        this.importPrice = importPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }


    public boolean isInStock() {
        return isInStock;
    }

    public void setInStock(boolean inStock) {
        isInStock = inStock;
    }


    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}