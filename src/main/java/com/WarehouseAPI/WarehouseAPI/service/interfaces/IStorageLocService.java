package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.model.StorageLocation;

import java.util.List;

public interface IStorageLocService {

    public String addStoLoc(StorageLocation storageLocation);
    public String updateStoLoc(String _id, StorageLocation storageLocation);
    public String deleteStoLoc(String _id);
    public StorageLocation getStoLoc(String _id);
    public List<StorageLocation> getAllStoloc();
    public List<StorageLocation> getEmptyStoLoc();
}
