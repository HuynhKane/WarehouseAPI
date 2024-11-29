package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.dto.ReportResponse;
import com.WarehouseAPI.WarehouseAPI.repository.ReportRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IReportService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReportService implements IReportService {

    @Autowired
    private final ReportRepository reportRepository;
    private final MongoTemplate mongoTemplate;
    public ReportService(ReportRepository reportRepository, MongoTemplate mongoTemplate){
        this.reportRepository = reportRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ResponseEntity<String> addReport(ReportResponse report) {
        try {
            return ResponseEntity.ok("Add Successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding product", e);
        }
    }

    @Override
    public ResponseEntity<String> updateReport(String _id, ReportResponse report) {

        try {

            return ResponseEntity.ok("Update Successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding product", e);
        }
    }

    @Override
    public ResponseEntity<String> deleteReport(String _id) {

        try {
            reportRepository.deleteById(_id);
            return ResponseEntity.ok("Delete Successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding product", e);
        }
    }

    @Override
    public ReportResponse getReport(String _id) {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("_id").is(new ObjectId(_id))),
                    Aggregation.lookup("user", "idSender", "_id", "sender"),
                    Aggregation.unwind("sender", true)
            );
            AggregationResults<ReportResponse> result = mongoTemplate.aggregate(
                    aggregation, "report", ReportResponse.class);
            return result.getUniqueMappedResult();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting report", e);
        }
    }


    @Override
    public List<ReportResponse> getAllReport() {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.lookup("user", "idSender", "_id", "sender"),
                    Aggregation.unwind("sender", true)
            );
            AggregationResults<ReportResponse> result = mongoTemplate.aggregate(
                    aggregation, "report", ReportResponse.class);
            return result.getMappedResults();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting report", e);
        }
    }
}
