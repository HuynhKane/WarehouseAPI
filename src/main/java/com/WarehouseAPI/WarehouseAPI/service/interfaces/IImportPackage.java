package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.model.response.ImportPackageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IImportPackage {

    public ResponseEntity<ImportPackage> addImportPackage(ImportPackage importPackage);
    public ResponseEntity<ImportPackage> updateImportPackage(String _id, ImportPackage importPackage);
    public ResponseEntity<ImportPackage> deleteImportPackage(String id);
    public ImportPackageResponse getImportPackage(String _id);
    public List<ImportPackageResponse> getAllImportPackages();
}
