package com.WarehouseAPI.WarehouseAPI.model;


import com.WarehouseAPI.WarehouseAPI.dto.ProductWithQuantity;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document(collection = "exportPackage")
public class ExportPackage {

    @Id
    private String id;
    private String packageName;
    private Date exportDate;
    private String deliveryMethod;
    @Field("idSender")
    private ObjectId idSender;
    @Field("customerId")
    private ObjectId customerId;
    private String statusDone;
    private String note;
    @Field("listProducts")
    private List<ProductWithQuantity> listProducts;
    private BigDecimal totalSellingPrice;


    public BigDecimal getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public ObjectId getIdSender() {
        return idSender;
    }

    public void setIdSender(ObjectId idSender) {
        this.idSender = idSender;
    }

    public ObjectId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(ObjectId customerId) {
        this.customerId = customerId;
    }

    public String getStatusDone() {
        return statusDone;
    }

    public void setStatusDone(String statusDone) {
        this.statusDone = statusDone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ProductWithQuantity> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<ProductWithQuantity> listProducts) {
        this.listProducts = listProducts;
    }
}
