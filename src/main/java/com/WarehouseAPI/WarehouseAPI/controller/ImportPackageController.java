package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.dto.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.service.ImportPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
        ResponseEntity<ImportPackageResponse> createdImportPackage = importPackageService.addPendingImportPackage(importPackage);
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

    @GetMapping("/pending/{id}")
    public ResponseEntity<ImportPackageResponse> getPendingImportPackageById(@PathVariable String id) {
        ImportPackageResponse importPackage = importPackageService.getPendingImportPackage(id);
        return new ResponseEntity<>(importPackage, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImportPackage> updateImportPackage(@PathVariable String id, @RequestParam("status") String status) {
        ResponseEntity<ImportPackage> updatedImportPackage = null;
        if(Objects.equals(status, "decline")){
            updatedImportPackage = importPackageService.updateDeclineImportPackage(id);
        } else if (Objects.equals(status, "approved")) {
            updatedImportPackage = importPackageService.updateImportPackage(id);
        }
        else {
            return null;
        }
        return updatedImportPackage;
    }

    @PutMapping("/{id}/update-products")
    public ResponseEntity<ImportPackage> updateProductsInImportPackage(@PathVariable String id, @RequestParam HashMap<String, String> storageLocationIds) {
        ResponseEntity<ImportPackage> importPackageResponseEntity = null;
        importPackageResponseEntity = importPackageService.updateProductImportPackage(id, storageLocationIds);
        return importPackageResponseEntity;
    }
//
//    @PutMapping("/{id}/update-products")
//    public ResponseEntity<ImportPackage> updateProductsInImportPackage(@PathVariable String id) {
//        ResponseEntity<ImportPackage> updatedImportPackage = null;
//            updatedImportPackage = importPackageService.updateProductInImportPackage( id);
//            return updatedImportPackage;
//        }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImportPackage(@PathVariable String id) {
        importPackageService.deleteImportPackage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
