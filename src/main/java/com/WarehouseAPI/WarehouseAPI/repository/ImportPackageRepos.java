package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImportPackageRepos extends MongoRepository<ImportPackage, String> {
}
