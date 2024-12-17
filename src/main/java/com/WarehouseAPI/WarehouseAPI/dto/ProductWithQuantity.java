package com.WarehouseAPI.WarehouseAPI.dto;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

public class ProductWithQuantity {

    @Field("productId")
    private ObjectId productId;

    @Field("quantity")
    private int quantity;

    public ProductWithQuantity(ObjectId productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public ProductWithQuantity() {
    }

    public ObjectId getProductId() {
        return productId;
    }

    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
