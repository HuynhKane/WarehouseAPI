package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.dto.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.model.StorageLocation;
import com.WarehouseAPI.WarehouseAPI.model.Supplier;
import com.WarehouseAPI.WarehouseAPI.service.ProductService;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IStorageLocService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storage-location")
public class StorageLocationController {

    private final IStorageLocService storageLocService;
    private final ProductService productService;

    public StorageLocationController(IStorageLocService storageLocService, ProductService productService) {
        this.storageLocService = storageLocService;
        this.productService = productService;
    }

    @GetMapping
    public List<StorageLocation> getAllStoLocDetails() {
        return storageLocService.getAllStoloc();
    }

    @GetMapping("/{id}")
    public StorageLocation getStoLocDetails(@PathVariable String id) {
        return storageLocService.getStoLoc(id);
    }


    @GetMapping("/search")
    public List<StorageLocation> getSearchedStoLocDetails(@RequestParam("value") String value){
        return storageLocService.findStoLocByName(value);
    }

    @GetMapping("/unused")
    public ResponseEntity<List<StorageLocation>> findUnusedStorageLocations() {
        List<StorageLocation> unusedLocations = storageLocService.getEmptyStoLoc();
        return ResponseEntity.ok(unusedLocations);
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