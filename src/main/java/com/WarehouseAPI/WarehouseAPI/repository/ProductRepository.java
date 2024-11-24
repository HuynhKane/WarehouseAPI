package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

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
    List<Product> findByStorageLocationId(String storageLocationId);
    List<Product> findByIsInStock(Boolean isInStock);
    List<Product> findBySupplierId(String supplierId);

}
