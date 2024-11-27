package com.WarehouseAPI.WarehouseAPI.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "storageLocation")
public class StorageLocation {

    @Id
    private String _id;

    private String storageLocationName;

    private String storageLocationImage;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStorageLocationName() {
        return storageLocationName;
    }

    public void setStorageLocationName(String storageLocationName) {
        this.storageLocationName = storageLocationName;
    }

    public String getStorageLocationImage() {
        return storageLocationImage;
    }

    public void setStorageLocationImage(String storageLocationImage) {
        this.storageLocationImage = storageLocationImage;
    }
}
