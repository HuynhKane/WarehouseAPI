package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import com.WarehouseAPI.WarehouseAPI.dto.ExportPackageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IExportPackage {
    public ResponseEntity<ExportPackage> addExportPackage(ExportPackageResponse exportPackage);
    public ResponseEntity<ExportPackage> updateExportPackage(String _id, ExportPackageResponse exportPackage);
    public ResponseEntity<ExportPackage> deleteExportPackage(String id);
    public ExportPackageResponse getExportPackage(String _id);
    public List<ExportPackageResponse> getAllExportPackages();
    public  List<ExportPackageResponse> getAllPendingPackages();
    public List<ExportPackageResponse> getAllDonePackages();
}
