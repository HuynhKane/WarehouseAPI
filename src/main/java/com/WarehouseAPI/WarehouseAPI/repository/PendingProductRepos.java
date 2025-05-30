package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.PendingProduct;
import com.WarehouseAPI.WarehouseAPI.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PendingProductRepos extends MongoRepository<PendingProduct, String> {
    public Optional<PendingProduct> findById(String id);
    List<PendingProduct> findAllByIdIn(List<String> ids);

}
