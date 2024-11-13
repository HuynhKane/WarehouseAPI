package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.StorageLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StorageLocationRepository extends MongoRepository<StorageLocation, String> {
}
