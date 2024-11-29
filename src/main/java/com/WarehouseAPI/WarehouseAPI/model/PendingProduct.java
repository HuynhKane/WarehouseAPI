package com.WarehouseAPI.WarehouseAPI.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "pendingProduct")
public class PendingProduct {

    @Id
    private ObjectId id;

    private String productName;
    @Field("genreId")
    private ObjectId genreId;
    private int quantity;
    private String description;
    private BigDecimal importPrice;
    private BigDecimal sellingPrice;
    @Field("supplierId")
    private ObjectId supplierId;
    private Date lastUpdated;
    private String image;

    public ObjectId getId() {
        return id;
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

    public ObjectId getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(ObjectId supplierId) {
        this.supplierId = supplierId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
