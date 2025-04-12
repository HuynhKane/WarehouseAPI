package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.dto.*;
import com.WarehouseAPI.WarehouseAPI.repository.ExportPackageRepos;
import com.WarehouseAPI.WarehouseAPI.repository.ImportPackageRepos;
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

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticService implements IStatisticService {


    @Autowired
    private StatisticRepository statisticRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    private ExportPackageRepos exportPackageRepos;
    @Autowired
    private ImportPackageRepos importPackageRepos;

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

    public List<MonthlyRevenue> getMonthlyRevenueByYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Lấy dữ liệu từ MongoDB
        List<MonthlyRevenue> revenueData =exportPackageRepos.getMonthlyRevenueByYear(start, end);

        // Tạo danh sách mặc định với giá trị 0 cho tất cả các tháng
        Map<Integer, BigDecimal> revenueMap = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            revenueMap.put(i, BigDecimal.ZERO);
        }

        // Gán dữ liệu từ MongoDB vào danh sách
        for (MonthlyRevenue data : revenueData) {
            revenueMap.put(data.getMonth(), data.getTotalRevenue());
        }

        // Chuyển thành danh sách kết quả
        List<MonthlyRevenue> result = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            MonthlyRevenue revenue = new MonthlyRevenue();
            revenue.setMonth(i);
            revenue.setTotalRevenue(revenueMap.get(i));
            result.add(revenue);
        }

        return result;
    }
    public List<MonthlyCost> getMonthlyCostByYear(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year + 1, 1, 1);

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<MonthlyCost> costData = importPackageRepos.getMonthlyCostByYear(start, end);

        Map<Integer, BigDecimal> costMap = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            costMap.put(i, BigDecimal.ZERO);
        }

        for (MonthlyCost data : costData) {
            BigDecimal cost = data.getTotalCost() != null ? data.getTotalCost() : BigDecimal.ZERO;
            costMap.put(data.getMonth(), cost);
        }

        List<MonthlyCost> result = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            MonthlyCost cost = new MonthlyCost();
            cost.setMonth(i);
            cost.setTotalCost(costMap.get(i));
            result.add(cost);
        }

        return result;
    }


    public List<ExportPackageInfo> getExportPackagesByMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return exportPackageRepos.getExportPackagesByMonth(start, end);
    }
    public List<PackageImportDetails> getImportPackagesByMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return importPackageRepos.getPackageImportDetailsByMonth(start, end);
    }


}
