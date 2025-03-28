package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.dto.ImportStatistic;
import com.WarehouseAPI.WarehouseAPI.repository.ExportPackageRepos;
import com.WarehouseAPI.WarehouseAPI.repository.StatisticRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IStatisticService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.util.Pair;


import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService implements IStatisticService {


    @Autowired
    private StatisticRepository statisticRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    private ExportPackageRepos exportPackageRepos;

    public StatisticService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Map<String, Integer> getGroupedImportStatistics(LocalDateTime startDate, LocalDateTime endDate, ChronoUnit unit) {
        List<ImportStatistic> statistics = statisticRepository.findImportStatistics(startDate, endDate);

        return statistics.stream()
                .collect(Collectors.groupingBy(
                        stat -> formatDateByUnit(stat.getImportDate(), unit),
                        Collectors.summingInt(ImportStatistic::getTotalProducts)
                ));
    }

    @Override
    public String formatDateByUnit(LocalDateTime date, ChronoUnit unit) {
        switch (unit) {
            case DAYS:
                return date.toLocalDate().toString();
            case WEEKS:
                return date.getYear() + "-W" + date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
            case MONTHS:
                return date.getYear() + "-" + date.getMonthValue();
            case YEARS:
                return String.valueOf(date.getYear());
            default:
                throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
    }

    public List<Map<String, Object>> getTopGenresImported(Pair<Long, Long> dateRange, int topN) {
        Long startMillis = dateRange.getFirst();
        Long endMillis = dateRange.getSecond();
        LocalDateTime startDate = (startMillis != null)
                ? Instant.ofEpochMilli(startMillis).atZone(ZoneOffset.UTC).toLocalDateTime()
                : null;
        LocalDateTime endDate = (endMillis != null)
                ? Instant.ofEpochMilli(endMillis).atZone(ZoneOffset.UTC).toLocalDateTime()
                : null;
        Criteria dateCriteria = new Criteria();

        if (startDate != null && endDate != null) {
            dateCriteria = Criteria.where("importDate").gte(startDate).lte(endDate);
        } else if (startDate != null) {
            dateCriteria = Criteria.where("importDate").gte(startDate);
        } else if (endDate != null) {
            dateCriteria = Criteria.where("importDate").lte(endDate);
        }
        Criteria statusCriteria = Criteria.where("statusDone").is("APPROVED");
        Criteria finalCriteria = new Criteria().andOperator(dateCriteria, statusCriteria);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(finalCriteria),  // Filter by importDate range
                Aggregation.unwind("listProducts"),  // Unwind listProducts array
                Aggregation.lookup("product", "listProducts", "_id", "productDetails"), // Lookup product details
                Aggregation.unwind("productDetails"),
                Aggregation.group("productDetails.genreId")
                        .sum("productDetails.quantity").as("total"), // Sum actual product quantities
                Aggregation.sort(Sort.Direction.DESC, "total"),
                Aggregation.limit(topN),
                Aggregation.lookup("genre", "_id", "_id", "genreDetails"), // Lookup genre info
                Aggregation.unwind("genreDetails") // Extract genre name
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "importPackage", Document.class);

        return results.getMappedResults().stream()
                .map(doc -> Map.<String, Object>of(
                        "genreId", doc.getObjectId("_id").toString(),  // Convert ObjectId to String
                        "genreName", doc.get("genreDetails", Document.class).getString("genreName"),  // Extract genre name
                        "total", doc.getInteger("total", 0) // Use correct total imported quantity
                ))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getTopGenresExported(Pair<Long, Long> dateRange, int topN) {
        Long startMillis = dateRange.getFirst();
        Long endMillis = dateRange.getSecond();
        LocalDateTime startDate = (startMillis != null)
                ? Instant.ofEpochMilli(startMillis).atZone(ZoneOffset.UTC).toLocalDateTime()
                : null;
        LocalDateTime endDate = (endMillis != null)
                ? Instant.ofEpochMilli(endMillis).atZone(ZoneOffset.UTC).toLocalDateTime()
                : null;
        Criteria dateCriteria = new Criteria();

        if (startDate != null && endDate != null) {
            dateCriteria = Criteria.where("exportDate").gte(startDate).lte(endDate);
        } else if (startDate != null) {
            dateCriteria = Criteria.where("exportDate").gte(startDate);
        } else if (endDate != null) {
            dateCriteria = Criteria.where("exportDate").lte(endDate);
        }
        Criteria statusCriteria = Criteria.where("statusDone").is("APPROVED");
        Criteria finalCriteria = new Criteria().andOperator(dateCriteria, statusCriteria);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(finalCriteria),
                Aggregation.unwind("listProducts"),
                Aggregation.lookup("product", "listProducts.productId", "_id", "productDetails"),
                Aggregation.unwind("productDetails"),
                Aggregation.group("productDetails.genreId")
                        .sum("listProducts.quantity").as("total"),
                Aggregation.sort(Sort.Direction.DESC, "total"),
                Aggregation.limit(topN),
                Aggregation.lookup("genre", "_id", "_id", "genreDetails"),
                Aggregation.unwind("genreDetails")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "exportPackage", Document.class);

        return results.getMappedResults().stream()
                .map(doc -> Map.<String, Object>of(
                        "genreId", doc.getObjectId("_id").toString(),
                        "genreName", doc.get("genreDetails", Document.class).getString("genreName"),
                        "total", doc.getInteger("total", 0)
                ))
                .collect(Collectors.toList());
    }
    public Double getTotalRevenue(String groupy, Pair<Long, Long> dateRange) {
        Long startMillis = dateRange.getFirst();
        Long endMillis = dateRange.getSecond();
        // Điều kiện lọc: chỉ lấy các đơn hàng APPROVED trong khoảng thời gian exportDate
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("exportDate").gte(startMillis).lte(endMillis),
                Criteria.where("statusDone").is("APPROVED")
        );

        // Aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group().sum("totalSellingPrice").as("totalRevenue")
        );

        // Thực hiện Aggregation
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "exportPackage", Document.class);

        // Lấy kết quả
        List<Document> mappedResults = results.getMappedResults();
        if (!mappedResults.isEmpty()) {
            return mappedResults.get(0).getDouble("totalRevenue");
        }
        return 0.0;
    }
}
