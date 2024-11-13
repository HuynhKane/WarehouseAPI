package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Report;
import com.WarehouseAPI.WarehouseAPI.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService implements IReportService{

    @Autowired
    ReportRepository reportRepository;
    public ReportService(ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }

    @Override
    public String addReport(Report report) {
        reportRepository.save(report);
        return "Add report, done";
    }

    @Override
    public String updateReport(String _id, Report report) {
        reportRepository.save(report);
        return "Update report, done";
    }

    @Override
    public String deleteReport(String _id) {
        reportRepository.deleteById(_id);
        return "Delete report, done";
    }

    @Override
    public Report getReport(String _id) {
        if(reportRepository.findById(_id).isEmpty())
            return null;
        return reportRepository.findById(_id).get();
    }

    @Override
    public List<Report> getAllReport() {
        return reportRepository.findAll();
    }
}
