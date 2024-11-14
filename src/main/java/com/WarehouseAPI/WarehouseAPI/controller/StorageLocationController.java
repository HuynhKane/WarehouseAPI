package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.StorageLocation;
import com.WarehouseAPI.WarehouseAPI.service.IStorageLocService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storage-location")
public class StorageLocationController {

    private final IStorageLocService storageLocService;

    public StorageLocationController(IStorageLocService storageLocService) {
        this.storageLocService = storageLocService;
    }

    @GetMapping
    public List<StorageLocation> getAllStoLocDetails() {
        return storageLocService.getAllStoloc();
    }

    @GetMapping("/{id}")
    public StorageLocation getStoLocDetails(@PathVariable String id) {
        return storageLocService.getStoLoc(id);
    }

    @PostMapping
    public String addStoLocDetails(@RequestBody StorageLocation storageLocation) {
        storageLocService.addStoLoc(storageLocation);
        return "Storage location added successfully";
    }

    @PutMapping("/{id}")
    public String updateStoLocDetails(@PathVariable String id, @RequestBody StorageLocation updated) {
        storageLocService.updateStoLoc(id, updated);
        return "Storage location updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteStoLocDetails(@PathVariable String id) {
        storageLocService.deleteStoLoc(id);
        return "Storage location deleted successfully";
    }
}