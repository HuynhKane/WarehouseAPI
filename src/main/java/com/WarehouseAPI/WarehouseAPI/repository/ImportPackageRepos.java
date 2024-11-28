package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.model.response.ImportPackageResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;

public interface ImportPackageRepos extends MongoRepository<ImportPackage, String> {
}
