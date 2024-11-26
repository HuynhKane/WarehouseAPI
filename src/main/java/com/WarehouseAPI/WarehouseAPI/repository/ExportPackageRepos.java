package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExportPackageRepos extends MongoRepository<ExportPackage, String> {
}
