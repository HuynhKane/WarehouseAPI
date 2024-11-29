package com.WarehouseAPI.WarehouseAPI.model;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "importPackage")
public class ImportPackage {
    @Id
    private String id;
    private String packageName;
    private LocalDateTime importDate;
    @Field("idReceiver")
    private ObjectId idReceiver;
    private String statusDone;
    private String note;
    @Field("listProducts")
    private List<ObjectId> listProducts;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ObjectId> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<ObjectId> listProducts) {
        this.listProducts = listProducts;
    }

    public ObjectId getIdReceiver() {
        return idReceiver;
    }

    public String getStatusDone() {
        return statusDone;
    }

    public void setStatusDone(String statusDone) {
        this.statusDone = statusDone;
    }

    public void setIdReceiver(ObjectId idReceiver) {
        this.idReceiver = idReceiver;
    }

}
