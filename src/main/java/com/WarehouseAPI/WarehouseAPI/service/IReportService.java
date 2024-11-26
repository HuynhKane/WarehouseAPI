package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Genre;
import com.WarehouseAPI.WarehouseAPI.model.Report;
import com.WarehouseAPI.WarehouseAPI.model.ReportResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReportService {
    public ResponseEntity<String> addReport(ReportResponse report);
    public ResponseEntity<String> updateReport(String _id, ReportResponse report);
    public ResponseEntity<String> deleteReport(String _id);
    public ReportResponse getReport(String _id);
    public List<ReportResponse> getAllReport();

}
