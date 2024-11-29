package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.dto.ReportResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IReportService {
    public ResponseEntity<String> addReport(ReportResponse report);
    public ResponseEntity<String> updateReport(String _id, ReportResponse report);
    public ResponseEntity<String> deleteReport(String _id);
    public ReportResponse getReport(String _id);
    public List<ReportResponse> getAllReport();

}
