package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.PendingProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PendingProductRepos extends MongoRepository<PendingProduct, String> {
}
