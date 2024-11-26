package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.model.response.ExportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.model.response.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.service.ExportPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/export-packages")
public class ExportPackageController {
    private final ExportPackageService exportPackageService;


    @Autowired
    public ExportPackageController(ExportPackageService exportPackageService) {
        this.exportPackageService = exportPackageService;
    }

    @PostMapping
    public ResponseEntity<ExportPackage> createExportPackage(@RequestBody ExportPackageResponse exportPackage) {
        ResponseEntity<ExportPackage> createdExportPackage = exportPackageService.addExportPackage(exportPackage);
        return createdExportPackage;
    }

    @GetMapping
    public ResponseEntity<List<ExportPackageResponse>> getAllExportPackages() {
        List<ExportPackageResponse> exportPackages = exportPackageService.getAllExportPackages();
        return new ResponseEntity<>(exportPackages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExportPackageResponse> getExportPackageById(@PathVariable String id) {
        ExportPackageResponse exportPackage = exportPackageService.getExportPackage(id);
        return new ResponseEntity<>(exportPackage, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExportPackage> updateExportPackage(@PathVariable String id, @RequestBody ExportPackageResponse exportPackageResponse) {
        ResponseEntity<ExportPackage> updatedPackage = exportPackageService.updateExportPackage( id, exportPackageResponse);
        return updatedPackage;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExportPackage(@PathVariable String id) {
        exportPackageService.deleteExportPackage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
