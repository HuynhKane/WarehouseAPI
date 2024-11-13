package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.model.Supplier;
import com.WarehouseAPI.WarehouseAPI.service.ISupplierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    ISupplierService iSupplierService;

    public  SupplierController(ISupplierService iSupplierService){
        this.iSupplierService = iSupplierService;
    }

    @GetMapping("/all")
    public List<Supplier> getAllSupplierDetails(){
        return iSupplierService.getAllSupplier();
    }

    @GetMapping("/{_id}/get")
    public Supplier getSupplierDetails(@PathVariable("_id") String _id){
        return iSupplierService.getSupplier(_id);
    }

    @PostMapping("/add")
    public String addSupplierDetails(@RequestBody Supplier supplier){
        iSupplierService.addSupplier(supplier);
        return "This was created successfully";
    }

    @PutMapping("/{_id}/update")
    public String updateSupplierDetails(@PathVariable("_id") String _id, @RequestBody Supplier updated){
        iSupplierService.updateSupplier(_id, updated);
        return "This was updated successfully";
    }

    @DeleteMapping("/{_id}/delete")
    public String deleteSupplierDetails(@PathVariable("_id") String _id){
        iSupplierService.deleteSupplier(_id);
        return "This was deleted successfully";
    }
}
