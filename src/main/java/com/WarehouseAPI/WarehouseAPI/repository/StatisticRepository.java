package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.dto.ImportStatistic;
import com.WarehouseAPI.WarehouseAPI.dto.Statistic;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository  extends MongoRepository<Statistic, String> {

    @Aggregation(pipeline = {
            "{ '$match': { 'importDate': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$unwind': '$listProducts' }",
            "{ '$group': { '_id': '$importDate', 'totalProducts': { '$sum': 1 } } }",
            "{ '$sort': { '_id': 1 } }"
    })
    List<ImportStatistic> findImportStatistics(LocalDateTime startDate, LocalDateTime endDate);
}
