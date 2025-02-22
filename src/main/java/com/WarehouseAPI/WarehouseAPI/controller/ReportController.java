package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.dto.ReportResponse;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final IReportService reportService;

    public ReportController(IReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public List<ReportResponse> getAllReportDetails() {
        return reportService.getAllReport();
    }

    @GetMapping("/{id}")
    public ReportResponse getReportDetails(@PathVariable String id) {
        return reportService.getReport(id);
    }

    @PostMapping
    public String addReportDetails(@RequestBody ReportResponse report) {
        reportService.addReport(report);
        return "Report added successfully";
    }

    @PutMapping("/{id}")
    public String updateReportDetails(@PathVariable String id, @RequestBody ReportResponse updatedReport) {
        reportService.updateReport(id, updatedReport);
        return "Report updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteReportDetails(@PathVariable String id) {
        reportService.deleteReport(id);
        return "Report deleted successfully";
    }
}