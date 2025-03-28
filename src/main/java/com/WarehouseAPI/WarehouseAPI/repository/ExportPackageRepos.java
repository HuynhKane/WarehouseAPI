package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.dto.MonthlyRevenue;
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


}
