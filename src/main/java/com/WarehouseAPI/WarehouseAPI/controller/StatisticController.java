package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.dto.StorageLocationSummary;
import com.WarehouseAPI.WarehouseAPI.service.StatisticService;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IProductService;

import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    private final IProductService productService;
    private final StatisticService statisticService;


    public StatisticController(IProductService productService, StatisticService statisticService) {
        this.productService = productService;
        this.statisticService = statisticService;
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
}
