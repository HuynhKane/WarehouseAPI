package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.model.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.model.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.repository.ImportPackageRepos;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportPackageService implements  IImportPackage{


    @Autowired
    private final ImportPackageRepos importPackageRepos;
    private final MongoTemplate mongoTemplate;

    public ImportPackageService(ImportPackageRepos importPackageRepos, MongoTemplate mongoTemplate) {
        this.importPackageRepos = importPackageRepos;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ResponseEntity<ImportPackage> addImportPackage(ImportPackage importPackage) {
        return null;
    }

    @Override
    public ResponseEntity<ImportPackage> updateImportPackage(String _id, ImportPackage importPackage) {
        return null;
    }

    @Override
    public ResponseEntity<ImportPackage> deleteImportPackage(String id) {
        return null;
    }

    @Override
    public ImportPackageResponse getImportPackage(String _id) {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("_id").is(new ObjectId(_id))),
                    Aggregation.lookup("user", "idReceiver", "_id", "receiver"),
                    Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                    Aggregation.lookup("product", "listProducts", "_id", "listProducts"),
                    Aggregation.unwind("receiver", true),
                    Aggregation.unwind("supplier", true)
            );
            AggregationResults<ImportPackageResponse> result = mongoTemplate.aggregate(
                    aggregation, "importPackage", ImportPackageResponse.class);
            return result.getUniqueMappedResult();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }

    @Override
    public List<ImportPackageResponse> getAllImportPakages() {
            try {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("user", "idReceiver", "_id", "receiver"),
                Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                Aggregation.lookup("product", "listProducts", "_id", "listProducts"),
                Aggregation.unwind("receiver", true),
                Aggregation.unwind("supplier", true)
        );
        AggregationResults<ImportPackageResponse> result = mongoTemplate.aggregate(
                aggregation, "importPackage", ImportPackageResponse.class);
        List<ImportPackageResponse> importPackages = result.getMappedResults();
        return importPackages;
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
    }
    }
}
