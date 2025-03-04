package com.WarehouseAPI.WarehouseAPI.repository;

import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExportPackageRepos extends MongoRepository<ExportPackage, String> {
    @Override
    List<ExportPackage> findAll();
}
