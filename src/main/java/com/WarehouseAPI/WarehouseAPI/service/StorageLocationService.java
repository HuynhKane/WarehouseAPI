package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.StorageLocation;
import com.WarehouseAPI.WarehouseAPI.repository.StorageLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageLocationService implements IStorageLocService{
    @Autowired
    StorageLocationRepository storageLocationRepository;

    public  StorageLocationService(StorageLocationRepository storageLocationRepository){
        this.storageLocationRepository = storageLocationRepository;
    }
    @Override
    public String addStoLoc(StorageLocation storageLocation) {
        storageLocationRepository.save(storageLocation);
        return "Add storage location, done";
    }

    @Override
    public String updateStoLoc(String _id, StorageLocation storageLocation) {
        storageLocationRepository.save(storageLocation);
        return "Update location, done";
    }

    @Override
    public String deleteStoLoc(String _id) {
        storageLocationRepository.deleteById(_id);
        return "Delete location, done";
    }

    @Override
    public StorageLocation getStoLoc(String _id) {
        if(storageLocationRepository.findById(_id).isEmpty())
            return null;
        return storageLocationRepository.findById(_id).get();
    }

    @Override
    public List<StorageLocation> getAllStoloc() {
        return storageLocationRepository.findAll();
    }
}
