package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByGenreId(String genreId);
    List<Product> findByProductName(String productName);
    List<Product> findByStorageLocationId(String storageLocationId);
    List<Product> findByIsInStock(Boolean isInStock);
}
