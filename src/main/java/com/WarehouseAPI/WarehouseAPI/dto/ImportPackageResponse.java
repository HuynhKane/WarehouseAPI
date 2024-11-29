package com.WarehouseAPI.WarehouseAPI.dto;
import com.WarehouseAPI.WarehouseAPI.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ImportPackageResponse {
    private String id;
    private String packageName;
    private LocalDateTime importDate;
    private User receiver;
    private String statusDone;
    private String note;
    private List<ProductResponse> listProducts;

    // Getters and Setters
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

    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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

    public List<ProductResponse> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<ProductResponse> listProducts) {
        this.listProducts = listProducts;
    }
}
