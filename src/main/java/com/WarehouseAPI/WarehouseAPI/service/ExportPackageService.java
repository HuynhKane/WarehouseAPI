package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import com.WarehouseAPI.WarehouseAPI.model.response.ExportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.model.response.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.model.response.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.repository.ExportPackageRepos;
import com.WarehouseAPI.WarehouseAPI.repository.ImportPackageRepos;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IExportPackage;
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

@Service
public class ExportPackageService implements IExportPackage {


    @Autowired
    private final ExportPackageRepos exportPackageRepos;
    private final MongoTemplate mongoTemplate;
    private final ProductService productService;

    public ExportPackageService(ExportPackageRepos exportPackageRepos, MongoTemplate mongoTemplate, ProductService productService) {
        this.exportPackageRepos = exportPackageRepos;
        this.mongoTemplate = mongoTemplate;
        this.productService = productService;
    }

    @Override
    public ResponseEntity<ExportPackage> addExportPackage(ExportPackageResponse exportPackage) {
        return null;
    }

    @Override
    public ResponseEntity<ExportPackage> updateExportPackage(String _id, ExportPackageResponse exportPackage) {
        return null;
    }

    @Override
    public ResponseEntity<ExportPackage> deleteExportPackage(String id) {
        return null;
    }

    @Override
    public ExportPackageResponse getExportPackage(String _id) {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("_id").is(new ObjectId(_id))),
                    Aggregation.lookup("user", "idSender", "_id", "sender"),
                    Aggregation.lookup("customer", "customerId", "_id", "customer"),
                    Aggregation.lookup("product", "listProducts", "_id", "listProducts"),
                    Aggregation.unwind("sender", true),
                    Aggregation.unwind("customer", true)
            );
            AggregationResults<ExportPackageResponse> result = mongoTemplate.aggregate(
                    aggregation, "exportPackage", ExportPackageResponse.class);

            ExportPackageResponse exportPackage = result.getUniqueMappedResult();
            List<ProductResponse> listProducts = new ArrayList<>();
            for (ProductResponse product : exportPackage.getListProducts()) {
                listProducts.add(productService.getProduct(product.getId()));
            }
            exportPackage.setListProducts(listProducts);
            return exportPackage;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }

    @Override
    public List<ExportPackageResponse> getAllExportPackages() {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.lookup("user", "idSender", "_id", "sender"),
                    Aggregation.lookup("customer", "customerId", "_id", "customer"),
                    Aggregation.lookup("product", "listProducts", "_id", "listProducts"),
                    Aggregation.unwind("sender", true),
                    Aggregation.unwind("customer", true)
            );
            AggregationResults<ExportPackageResponse> result = mongoTemplate.aggregate(
                    aggregation, "exportPackage", ExportPackageResponse.class);
            List<ExportPackageResponse> exportPackages = result.getMappedResults();
            for (ExportPackageResponse exportPackage : exportPackages) {
                List<ProductResponse> listProducts = new ArrayList<>();
                for (ProductResponse product : exportPackage.getListProducts()) {
                    listProducts.add(productService.getProduct(product.getId()));
                }
                exportPackage.setListProducts(listProducts);
            }
            return exportPackages;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }
}
