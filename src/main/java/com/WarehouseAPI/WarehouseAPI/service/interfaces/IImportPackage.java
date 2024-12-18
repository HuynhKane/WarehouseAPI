package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.dto.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IImportPackage {

    public ResponseEntity<ImportPackageResponse> addPendingImportPackage(ImportPackageResponse importPackage);

    ResponseEntity<ImportPackage> updateDeclineImportPackage(String _id);

    ResponseEntity<ImportPackage> updateProductInImportPackage(String _id);

    public ResponseEntity<ImportPackage> updateImportPackage(String _id);
    public ResponseEntity<ImportPackageResponse> deleteImportPackage(String id);
    public ImportPackageResponse getImportPackage(String _id);
    public List<ImportPackageResponse> getAllImportPackages();
    public List<ImportPackageResponse> getAllPendingPackages();
    public List<ImportPackageResponse> getAllDonePackages();
    public List<ImportPackageResponse> getAllCancelledPackages();
    public String approvePendingProduct(ObjectId pendingProductId);
    public ImportPackageResponse getPendingImportPackage(String _id);
}
