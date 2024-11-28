package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.model.response.ProductInStorageLocation;
import com.WarehouseAPI.WarehouseAPI.model.response.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.model.response.StorageLocationSummary;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    private final IProductService productService;

    public StatisticController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/in-stock")
    public List<StorageLocationSummary> getStockSummaryByLocation() {
        return productService.getStockSummaryByLocation();
    }
}
