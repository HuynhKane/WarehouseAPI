package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.dto.MonthlyRevenue;
import com.WarehouseAPI.WarehouseAPI.dto.PackageInfo;
import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExportPackageRepos extends MongoRepository<ExportPackage, String> {
    @Override
    List<ExportPackage> findAll();
    @Aggregation(pipeline = {
            "{ $match: { statusDone: 'APPROVED', exportDate: { $gte: ?0, $lt: ?1 } } }",
            "{ $group: { _id: { month: { $month: '$exportDate' } }, totalRevenue: { $sum: { $toDouble: '$totalSellingPrice' } } } }",
            "{ $project: { _id: 0, month: '$_id.month', totalRevenue: 1 } }",
            "{ $sort: { month: 1 } }"
    })
    List<MonthlyRevenue> getMonthlyRevenueByYear(Date startDate, Date endDate);

    @Aggregation(pipeline = {
            "{ $match: { statusDone: 'APPROVED', exportDate: { $gte: ?0, $lt: ?1 } } }",
            "{ $project: { _id: 1, packageName: 1, totalSellingPrice: { $toDouble: '$totalSellingPrice' } } }",
            "{ $sort: { totalSellingPrice: -1 } }"
    })
    List<PackageInfo> getExportPackagesByMonth(Date startDate, Date endDate);
}
