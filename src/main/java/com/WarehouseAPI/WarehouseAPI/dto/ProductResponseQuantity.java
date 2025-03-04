package com.WarehouseAPI.WarehouseAPI.dto;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

public class ProductResponseQuantity {

    private ProductResponse product ;

    @Field("quantity")
    private int quantity;

    public ProductResponseQuantity(ProductResponse product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductResponseQuantity() {

    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
