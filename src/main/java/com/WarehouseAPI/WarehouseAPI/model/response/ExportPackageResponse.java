package com.WarehouseAPI.WarehouseAPI.model.response;

import com.WarehouseAPI.WarehouseAPI.model.Customer;
import com.WarehouseAPI.WarehouseAPI.model.Supplier;
import com.WarehouseAPI.WarehouseAPI.model.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ExportPackageResponse {

    private String id;
    private String packageName;
    private Date exportDate;
    private User sender;
    private Customer customer;
    private String deliveryMethod;
    private boolean statusDone;
    private String note;
    private List<ProductResponse> listProducts;

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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public boolean isStatusDone() {
        return statusDone;
    }

    public void setStatusDone(boolean statusDone) {
        this.statusDone = statusDone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ProductResponse> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<ProductResponse> listProducts) {
        this.listProducts = listProducts;
    }
}
