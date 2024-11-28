package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.model.response.ImportPackageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IImportPackage {

    public ResponseEntity<ImportPackageResponse> addImportPackage(ImportPackageResponse importPackage);
    public ResponseEntity<ImportPackageResponse> updateImportPackage(String _id, ImportPackageResponse importPackage);
    public ResponseEntity<ImportPackageResponse> deleteImportPackage(String id);
    public ImportPackageResponse getImportPackage(String _id);
    public List<ImportPackageResponse> getAllImportPackages();
    public List<ImportPackageResponse> getAllPendingPackages();
    public List<ImportPackageResponse> getAllDonePackages();
    public List<ImportPackageResponse> getAllCancelledPackages();
}
