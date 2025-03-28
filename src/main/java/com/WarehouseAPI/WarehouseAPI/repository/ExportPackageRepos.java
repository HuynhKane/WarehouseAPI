package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;

public interface ExportPackageRepos extends MongoRepository<ExportPackage, String> {
    @Override
    List<ExportPackage> findAll();
    @Aggregation(pipeline = {
            "{ $match: { 'exportDate': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$statusDone', totalRevenue: { $sum: '$totalSellingPrice' } } }"
    })
    List<Map<String, Object>> getRevenueByDateRange(long startDate, long endDate);
}
