package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.model.response.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.repository.ImportPackageRepos;
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
    public ResponseEntity<ImportPackageResponse> createImportPackage(@RequestBody ImportPackageResponse importPackage) {
        ResponseEntity<ImportPackageResponse> createdImportPackage = importPackageService.addImportPackage(importPackage);
        return createdImportPackage;
    }

    @GetMapping
    public ResponseEntity<List<ImportPackageResponse>> getAllImportPackages() {
        List<ImportPackageResponse> importPackages = importPackageService.getAllImportPackages();
        return new ResponseEntity<>(importPackages, HttpStatus.OK);
    }
    @GetMapping("/pending")
    public ResponseEntity<List<ImportPackageResponse>> getAllPendingPackages() {
        List<ImportPackageResponse> importPackages = importPackageService.getAllPendingPackages();
        return new ResponseEntity<>(importPackages, HttpStatus.OK);
    }
    @GetMapping("/done")
    public ResponseEntity<List<ImportPackageResponse>> getAllDonePackages() {
        List<ImportPackageResponse> importPackages = importPackageService.getAllDonePackages();
        return new ResponseEntity<>(importPackages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImportPackageResponse> getImportPackageById(@PathVariable String id) {
        ImportPackageResponse importPackage = importPackageService.getImportPackage(id);
        return new ResponseEntity<>(importPackage, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImportPackageResponse> updateImportPackage(@PathVariable String id, @RequestBody ImportPackageResponse importPackage) {
        ResponseEntity<ImportPackageResponse> updatedImportPackage = importPackageService.updateImportPackage( id, importPackage);
        return updatedImportPackage;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImportPackage(@PathVariable String id) {
        importPackageService.deleteImportPackage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
