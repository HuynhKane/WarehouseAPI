package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Supplier;
import com.WarehouseAPI.WarehouseAPI.service.ISupplierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    private final ISupplierService supplierService;

    public SupplierController(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public List<Supplier> getAllSupplierDetails() {
        return supplierService.getAllSupplier();
    }

    @GetMapping("/{id}")
    public Supplier getSupplierDetails(@PathVariable String id) {
        return supplierService.getSupplier(id);
    }

    @PostMapping
    public String addSupplierDetails(@RequestBody Supplier supplier) {
        supplierService.addSupplier(supplier);
        return "Supplier added successfully";
    }

    @PutMapping("/{id}")
    public String updateSupplierDetails(@PathVariable String id, @RequestBody Supplier updatedSupplier) {
        supplierService.updateSupplier(id, updatedSupplier);
        return "Supplier updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteSupplierDetails(@PathVariable String id) {
        supplierService.deleteSupplier(id);
        return "Supplier deleted successfully";
    }
}