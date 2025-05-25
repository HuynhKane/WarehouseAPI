package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.PendingProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PendingProductRepository extends MongoRepository<PendingProduct, String> {
    List<PendingProduct> findAllByIdIn(List<String> ids);
}