package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.model.response.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.model.response.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.repository.ImportPackageRepos;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IImportPackage;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImportPackageService implements IImportPackage {


    @Autowired
    private final ImportPackageRepos importPackageRepos;
    private final MongoTemplate mongoTemplate;
    private final ProductService productService;
    private final ProductRepository productRepository;


    public ImportPackageService(ImportPackageRepos importPackageRepos, MongoTemplate mongoTemplate, ProductService productService, ProductRepository productRepository) {
        this.importPackageRepos = importPackageRepos;
        this.mongoTemplate = mongoTemplate;
        this.productService = productService;

        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<ImportPackageResponse> addImportPackage(ImportPackageResponse importPackage) {
        try{
            ImportPackage importPackage_save = new ImportPackage();
            importPackage_save.setPackageName(importPackage.getPackageName());
            importPackage_save.setImportDate(importPackage.getImportDate());
            importPackage_save.setNote(importPackage.getNote());
            importPackage_save.setIdReceiver(new ObjectId(importPackage.getReceiver().get_id()));
            importPackage_save.setSupplierId(new ObjectId(importPackage.getSupplier().get_id()));
            importPackage_save.setStatusDone(importPackage.isStatusDone());
            List<ProductResponse> productResponseList = importPackage.getListProducts();
            List<ObjectId> productIdList = new ArrayList<>();
            for (ProductResponse productResponse : productResponseList){
                if(productRepository.findById(productResponse.getId()).isEmpty()) {
                    productService.addProduct(productResponse);
                }
                productIdList.add(new ObjectId(productResponse.getId()));
            }
            importPackage_save.setListProducts(productIdList);
            importPackageRepos.save(importPackage_save);


        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding import package", e);
        }
        return null;
    }

    @Override
    public ResponseEntity<ImportPackageResponse> updateImportPackage(String _id, ImportPackageResponse importPackage) {
        try {
            Optional<ImportPackage> existingPackage = importPackageRepos.findById(_id);
            if (existingPackage.isPresent()) {
                ImportPackage importPackage_save = existingPackage.get();
                importPackage_save.setPackageName(importPackage.getPackageName());
                importPackage_save.setImportDate(importPackage.getImportDate());
                importPackage_save.setNote(importPackage.getNote());
                importPackage_save.setIdReceiver(new ObjectId(importPackage.getReceiver().get_id()));
                importPackage_save.setSupplierId(new ObjectId(importPackage.getSupplier().get_id()));
                importPackage_save.setStatusDone(importPackage.isStatusDone());
                List<ProductResponse> productResponseList = importPackage.getListProducts();
                List<ObjectId> productIdList = new ArrayList<>();
                for (ProductResponse productResponse : productResponseList){
                    productIdList.add(new ObjectId(productResponse.getId()));
                }
                importPackage_save.setListProducts(productIdList);
                importPackageRepos.save(importPackage_save);
                return ResponseEntity.ok(importPackage);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating import package", e);
        }
    }

    @Override
    public ResponseEntity<ImportPackageResponse> deleteImportPackage(String id) {
        try {
            importPackageRepos.deleteById(id);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding import package", e);
        }
        return null;
    }

    @Override
    public ImportPackageResponse getImportPackage(String _id) {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("_id").is(new ObjectId(_id))),
                    Aggregation.lookup("user", "idReceiver", "_id", "receiver"),
                    Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                    Aggregation.lookup("product", "listProducts", "_id", "listProducts"),
                    Aggregation.unwind("receiver", true),
                    Aggregation.unwind("supplier", true)
            );
            AggregationResults<ImportPackageResponse> result = mongoTemplate.aggregate(
                    aggregation, "importPackage", ImportPackageResponse.class);

            ImportPackageResponse importPackage = result.getUniqueMappedResult();
            List<ProductResponse> listProducts = new ArrayList<>();
            for (ProductResponse product : importPackage.getListProducts()) {
                listProducts.add(productService.getProduct(product.getId()));
            }
            importPackage.setListProducts(listProducts);
            return importPackage;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }

    @Override
    public List<ImportPackageResponse> getAllImportPackages() {
            try {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("user", "idReceiver", "_id", "receiver"),
                Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                Aggregation.lookup("product", "listProducts", "_id", "listProducts"),
                Aggregation.unwind("receiver", true),
                Aggregation.unwind("supplier", true)
        );
        AggregationResults<ImportPackageResponse> result = mongoTemplate.aggregate(
                aggregation, "importPackage", ImportPackageResponse.class);
        List<ImportPackageResponse> importPackages = result.getMappedResults();
        for (ImportPackageResponse importPackage : importPackages) {
            List<ProductResponse> listProducts = new ArrayList<>();
            for (ProductResponse product : importPackage.getListProducts()) {
                listProducts.add(productService.getProduct(product.getId()));
            }
            importPackage.setListProducts(listProducts);
        }
        return importPackages;
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
    }
    }

    @Override
    public List<ImportPackageResponse> getAllPendingPackages() {
        try {
            List<ImportPackageResponse> pendingList = new ArrayList<>();
            for (ImportPackageResponse importPackage : getAllImportPackages()) {
                if (!importPackage.isStatusDone()) {
                    pendingList.add(importPackage);
                }
            }
            return pendingList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }

    @Override
    public List<ImportPackageResponse> getAllDonePackages() {
        try {
            List<ImportPackageResponse> doneList = new ArrayList<>();
            for (ImportPackageResponse importPackage : getAllImportPackages()) {
                if (importPackage.isStatusDone()) {
                    doneList.add(importPackage);
                }
            }
            return doneList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }

    @Override
    public List<ImportPackageResponse> getAllCancelledPackages() {
        return List.of();
    }
}
