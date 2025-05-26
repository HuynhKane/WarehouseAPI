package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.dto.StorageLocationSummary;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Aggregation(pipeline = {
            "{ $lookup: { from: 'supplier', localField: 'supplierId', foreignField: '_id', as: 'supplier' } }",
            "{ $lookup: { from: 'genre', localField: 'genreId', foreignField: '_id', as: 'genre' } }",
            "{ $lookup: { from: 'storageLocation', localField: 'storageLocationId', foreignField: '_id', as: 'storageLocation' } }",
            "{ $unwind: { path: '$supplier', preserveNullAndEmptyArrays: true } }",
            "{ $unwind: { path: '$storageLocation', preserveNullAndEmptyArrays: true } }",
            "{ $unwind: { path: '$genre', preserveNullAndEmptyArrays: true } }"
    })
    List<Product> findProductByGenreId(String genreId);
    List<Product> findByProductName(String productName);
    List<Product> findByProductNameContainingIgnoreCase(String value);
    List<Product> findByIsInStock(Boolean isInStock);
    List<Product> findBySupplierId(String supplierId);
    @Aggregation(pipeline = {
            "{ '$match': { 'isInStock': true } }",
            "{ '$group': { '_id': '$storageLocationId', 'totalQuantity': { '$sum': '$quantity' } } }",
            "{ '$lookup': { 'from': 'storageLocation', 'localField': '_id', 'foreignField': '_id', 'as': 'storageLocationDetails' } }",
            "{ '$unwind': '$storageLocationDetails' }",
            "{ '$project': { '_id': 0, 'storageLocationId': '$_id', 'storageLocationName': '$storageLocationDetails.storageLocationName', 'totalQuantity': 1 } }"
    })
    List<StorageLocationSummary> getStockSummaryByLocation();
    long countBy_idIn(List<String> ids);
    List<Product> findByQuantityLessThan(int quantity);
    List<Product> findByStorageLocationId(Object storageLocationId);



}
