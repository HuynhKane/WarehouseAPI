package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.dto.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import com.WarehouseAPI.WarehouseAPI.dto.ExportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
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

    @PostMapping("/pending")
    public ResponseEntity<ExportPackage> createExportPackage(@RequestBody ExportPackage exportPackage) {
        ResponseEntity<ExportPackage> createdExportPackage = exportPackageService.addPendingExportPackage(exportPackage);
        return createdExportPackage;
    }
    @PutMapping("/approve/{packageId}")
    public ResponseEntity<ExportPackage> approveExportPackage(@PathVariable String packageId) {
        return exportPackageService.approveExportPackage(packageId);
    }

    @PutMapping("/pending/{id}/update")
    public ResponseEntity<ExportPackage> updatePendingExportPackageById(@PathVariable String id, @RequestBody ExportPackageResponse packageResponse) {
        ExportPackage exportPackage = exportPackageService.updateInforPendingPackage(id, packageResponse).getBody();
        return new ResponseEntity<>(exportPackage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ExportPackageResponse>> getAllExportPackages() {
        List<ExportPackageResponse> exportPackages = exportPackageService.getAllExportPackages();
        return new ResponseEntity<>(exportPackages, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ExportPackageResponse>> getAllPendingExportPackages() {
        List<ExportPackageResponse> exportPackages = exportPackageService.getAllPendingPackages();
        return new ResponseEntity<>(exportPackages, HttpStatus.OK);
    }
    @GetMapping("/done")
    public ResponseEntity<List<ExportPackageResponse>> getAllDoneExportPackages() {
        List<ExportPackageResponse> exportPackages = exportPackageService.getAllDonePackages();
        return new ResponseEntity<>(exportPackages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExportPackageResponse> getExportPackageById(@PathVariable String id) {
        ExportPackageResponse exportPackage = exportPackageService.getExportPackage(id);
        return new ResponseEntity<>(exportPackage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExportPackage(@PathVariable String id) {
        exportPackageService.deleteExportPackage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
