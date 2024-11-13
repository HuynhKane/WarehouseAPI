package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.model.StorageLocation;
import com.WarehouseAPI.WarehouseAPI.service.IStorageLocService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storage-location")
public class StorageLocationController {
    IStorageLocService iStorageLocService;

    public StorageLocationController(IStorageLocService iStorageLocService){
        this.iStorageLocService = iStorageLocService;
    }
    @GetMapping("/all")
    public List<StorageLocation> getAllStoLocDetails(){
        return iStorageLocService.getAllStoloc();
    }

    @GetMapping("/{_id}/get")
    public StorageLocation getStoLocDetails(@PathVariable("_id") String _id){
        return iStorageLocService.getStoLoc(_id);
    }

    @PostMapping("/add")
    public String addStoLocDetails(@RequestBody StorageLocation storageLocation){
        iStorageLocService.addStoLoc(storageLocation);
        return "Storage location was created successfully";
    }

    @PutMapping("/{_id}/update")
    public String updateStoLocDetails(@PathVariable("_id") String _id, @RequestBody StorageLocation updated){
        iStorageLocService.updateStoLoc(_id, updated);
        return "Storage location was updated successfully";
    }

    @DeleteMapping("/{_id}/delete")
    public String deleteStoLocDetails(@PathVariable("_id") String _id){
        iStorageLocService.deleteStoLoc(_id);
        return "Storage location was deleted successfully";
    }
}
