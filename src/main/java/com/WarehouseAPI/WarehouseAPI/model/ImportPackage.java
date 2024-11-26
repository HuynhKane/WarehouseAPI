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
    private boolean statusDone;
    private String note;
    @Field("supplierId")
    private ObjectId supplierId;
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


    public List<ObjectId> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<ObjectId> listProducts) {
        this.listProducts = listProducts;
    }

    public ObjectId getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(ObjectId idReceiver) {
        this.idReceiver = idReceiver;
    }

    public ObjectId getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(ObjectId supplierId) {
        this.supplierId = supplierId;
    }
}