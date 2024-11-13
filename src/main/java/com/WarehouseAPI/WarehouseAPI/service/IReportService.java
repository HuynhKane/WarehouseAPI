package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Genre;
import com.WarehouseAPI.WarehouseAPI.model.Report;

import java.util.List;

public interface IReportService {
    public String addReport(Report report);
    public String updateReport(String _id, Report report);
    public String deleteReport(String _id);
    public Report getReport(String _id);
    public List<Report> getAllReport();

}
