package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.dto.GenreByDateResponse;
import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import com.WarehouseAPI.WarehouseAPI.dto.ExportPackageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IExportPackage {

    ResponseEntity<ExportPackage> addPendingExportPackage(ExportPackage exportPackage);
    ResponseEntity<ExportPackage> approveExportPackage(String packageId);
    public ResponseEntity<ExportPackage> deleteExportPackage(String id);
    public ExportPackageResponse getExportPackage(String _id);
    public List<ExportPackageResponse> getAllExportPackages();
    public  List<ExportPackageResponse> getAllPendingPackages();
    public List<ExportPackageResponse> getAllDonePackages();
    public List<GenreByDateResponse> getGenreExportByDate(String date);
}
