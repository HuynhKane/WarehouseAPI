package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.model.Report;
import com.WarehouseAPI.WarehouseAPI.service.IReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/report")
public class ReportController {

    IReportService iReportService;

    public ReportController(IReportService iReportService){
        this.iReportService = iReportService;
    }

    @GetMapping("/all")
    public List<Report> getAllReportDetails(){
        return iReportService.getAllReport();
    }

    @GetMapping("/{_id}/get")
    public Report getReportDetails(@PathVariable("_id") String _id){
        return iReportService.getReport(_id);
    }

    @PostMapping("/add")
    public String addReportDetails(@RequestBody Report report){
        iReportService.addReport(report);
        return "Report was created successfully";
    }

    @PutMapping("/{_id}/update")
    public String updateReportDetails(@PathVariable("_id") String _id, @RequestBody Report updatedReport){
        iReportService.updateReport(_id, updatedReport);
        return "Report was updated successfully";
    }

    @DeleteMapping("/{_id}/delete")
    public String deleteReportDetails(@PathVariable("_id") String _id){
        iReportService.deleteReport(_id);
        return "Report was deleted successfully";
    }

}
