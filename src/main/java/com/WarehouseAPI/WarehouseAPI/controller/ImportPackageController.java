package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.model.response.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.service.ImportPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/import-packages")
public class ImportPackageController {
    private final ImportPackageService importPackageService;

    @Autowired
    public ImportPackageController(ImportPackageService importPackageService) {
        this.importPackageService = importPackageService;
    }
    @PostMapping
    public ResponseEntity<ImportPackage> createImportPackage(@RequestBody ImportPackage importPackage) {
        ResponseEntity<ImportPackage> createdImportPackage = importPackageService.addImportPackage(importPackage);
        return createdImportPackage;
    }

    @GetMapping
    public ResponseEntity<List<ImportPackageResponse>> getAllImportPackages() {
        List<ImportPackageResponse> importPackages = importPackageService.getAllImportPackages();
        return new ResponseEntity<>(importPackages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportPackageResponse> getImportPackageById(@PathVariable String id) {
        ImportPackageResponse importPackage = importPackageService.getImportPackage(id);
        return new ResponseEntity<>(importPackage, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImportPackage> updateImportPackage(@PathVariable String id, @RequestBody ImportPackage importPackage) {
        ResponseEntity<ImportPackage> updatedImportPackage = importPackageService.updateImportPackage( id, importPackage);
        return updatedImportPackage;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImportPackage(@PathVariable String id) {
        importPackageService.deleteImportPackage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
