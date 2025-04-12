package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.dto.*;
import com.WarehouseAPI.WarehouseAPI.service.ExportPackageService;
import com.WarehouseAPI.WarehouseAPI.service.StatisticService;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IProductService;

import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    private final IProductService productService;
    private final StatisticService statisticService;
    private final ExportPackageService exportPackageService;


    public StatisticController(IProductService productService, StatisticService statisticService, ExportPackageService exportPackageService) {
        this.productService = productService;
        this.statisticService = statisticService;
        this.exportPackageService = exportPackageService;
    }

    @GetMapping("/in-stock")
    public List<StorageLocationSummary> getStockSummaryByLocation() {
        return productService.getStockSummaryByLocation();
    }

    @GetMapping("/import-history/grouped")
    public Map<String, Integer> getGroupedImportStatistics(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String unit) {

        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        ChronoUnit chronoUnit;
        switch (unit.toLowerCase()) {
            case "day":
                chronoUnit = ChronoUnit.DAYS;
                break;
            case "week":
                chronoUnit = ChronoUnit.WEEKS;
                break;
            case "month":
                chronoUnit = ChronoUnit.MONTHS;
                break;
            case "year":
                chronoUnit = ChronoUnit.YEARS;
                break;
            default:
                throw new IllegalArgumentException("Invalid unit: " + unit);
        }

        return statisticService.getGroupedImportStatistics(start, end, chronoUnit);
    }
    @GetMapping("/top-genres-imported")
    public List<Map<String, Object>> getTopGenresImported(
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return statisticService.getTopGenresImported(Pair.of(startDate, endDate), limit);
    }
    @GetMapping("/top-genres-exported")
    public List<Map<String, Object>> getTopGenresEported(
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return statisticService.getTopGenresExported(Pair.of(startDate, endDate), limit);
    }

    @GetMapping("/monthly-revenue/{year}")
    public List<MonthlyRevenue> getMonthlyRevenue(@PathVariable int year) {
        return statisticService.getMonthlyRevenueByYear(year);
    }

    // 2️⃣ API lấy danh sách ExportPackage theo tháng
    @GetMapping("/monthly-revenue/{year}/{month}")
    public List<ExportPackageInfo> getPackagesByMonth(@PathVariable int year, @PathVariable int month) {
        return statisticService.getExportPackagesByMonth(year, month);
    }
    @GetMapping("/monthly-cost/{year}")
    public List<MonthlyCost> getMonthlyCost(@PathVariable int year) {
        return statisticService.getMonthlyCostByYear(year);
    }

    @GetMapping("/monthly-cost/{year}/{month}")
    public List<PackageImportDetails> getMonthlyCost(@PathVariable int year, @PathVariable int month) {
        return statisticService.getImportPackagesByMonth(year,month);
    }



}
