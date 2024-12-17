package com.WarehouseAPI.WarehouseAPI.dto;

import com.WarehouseAPI.WarehouseAPI.model.PendingProduct;
import com.WarehouseAPI.WarehouseAPI.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class PendingImportPackageResponse {
    private String id;
    private String packageName;
    private LocalDateTime importDate;
    private User receiver;
    private String statusDone;
    private String note;
    private List<PendingProduct> listProducts;

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

    public List<PendingProduct> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<PendingProduct> listProducts) {
        this.listProducts = listProducts;
    }
}
