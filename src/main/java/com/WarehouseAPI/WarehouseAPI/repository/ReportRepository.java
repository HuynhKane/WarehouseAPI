package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.Report;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<Report, String> {
    @Aggregation(pipeline = {
            "{ $lookup: { from: 'user', localField: 'idSender', foreignField: '_id', as: 'user' } }",
            "{ $unwind: { path: '$user', preserveNullAndEmptyArrays: true } }"
    })
    List<Report> getReportByStatus(String status);

}
