package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.StorageLocation;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
import com.WarehouseAPI.WarehouseAPI.repository.StorageLocationRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IStorageLocService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StorageLocationService implements IStorageLocService {
    @Autowired
    private final StorageLocationRepository storageLocationRepository;
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;
    public  StorageLocationService(StorageLocationRepository storageLocationRepository, ProductRepository productRepository, MongoTemplate mongoTemplate){
        this.storageLocationRepository = storageLocationRepository;
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public String addStoLoc(StorageLocation storageLocation) {
        storageLocationRepository.save(storageLocation);
        return "Add storage location, done";
    }

    @Override
    public String updateStoLoc(String _id, StorageLocation storageLocation) {
        storageLocationRepository.save(storageLocation);
        return "Update location, done";
    }

    @Override
    public String deleteStoLoc(String _id) {
        storageLocationRepository.deleteById(_id);
        return "Delete location, done";
    }

    @Override
    public StorageLocation getStoLoc(String _id) {
        if(storageLocationRepository.findById(_id).isEmpty())
            return null;
        return storageLocationRepository.findById(_id).get();
    }

    @Override
    public List<StorageLocation> getAllStoloc() {
        return storageLocationRepository.findAll();
    }
    @Override
    public List<StorageLocation> findStoLocByName(String value){
        try {
            Criteria criteria = Criteria.where("storageLocationName").regex(value, "i");
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(criteria));

            AggregationResults<StorageLocation> results = mongoTemplate.aggregate(
                    aggregation, "storageLocation", StorageLocation.class);
            return results.getMappedResults();
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching storage", e);
        }

    }

    @Override
    public List<StorageLocation> getEmptyStoLoc() {
//        List<StorageLocation> unusedStorageLocations = new ArrayList<>();
//        List<String> usedStorageLocations = new ArrayList<>();
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.group("storageLocationId")
//                        .first("storageLocationId").as("storageLocationId")
//        );
//        AggregationResults<String> results;
//        try {
//            results = mongoTemplate.aggregate(aggregation, "product", String.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Error during aggregation query: " + e.getMessage(), e);
//        }
//        List<String> ids = results.getMappedResults();
//        if (ids == null || ids.isEmpty()) {
//            throw new RuntimeException("No storage locations found in the aggregation results.");
//        }
//        for (String id : ids) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> map = null;
//            try {
//                map = objectMapper.readValue(id, Map.class);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException("Error parsing aggregation result: " + e.getMessage(), e);
//            }
//            if (map == null || !map.containsKey("storageLocationId")) {
//                throw new RuntimeException("Invalid aggregation result: Missing 'storageLocationId' key.");
//            }
//            Map<String, String> storageLocationId = (Map<String, String>) map.get("storageLocationId");
//            String oid = storageLocationId.get("$oid");
//            if (oid != null) {
//                usedStorageLocations.add(oid);
//            } else {
//                throw new RuntimeException("Invalid storage location ID in aggregation result.");
//            }
//        }
//
//        List<StorageLocation> storageLocations;
//        storageLocations = storageLocationRepository.findAll();
//        if (storageLocations == null || storageLocations.isEmpty()) {
//            throw new RuntimeException("No storage locations found in the database.");
//        }
//        for (StorageLocation storageLocation : storageLocations) {
//            if (storageLocation != null && !usedStorageLocations.contains(storageLocation.get_id())) {
//                unusedStorageLocations.add(storageLocation);
//            }
//        }
//
//        return unusedStorageLocations;
//    }
//
//
        List<ObjectId> usedStorageLocationIds = mongoTemplate.findDistinct(
                Query.query(Criteria.where("storageLocationId").exists(true)),
                "storageLocationId",
                "product",
                ObjectId.class
        );
        List<StorageLocation> allStorageLocations = storageLocationRepository.findAll();
        return allStorageLocations.stream()
                .filter(storageLocation -> !usedStorageLocationIds.contains(new ObjectId(storageLocation.get_id())))
                .collect(Collectors.toList());
    }
}
