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
import java.util.Optional;

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
        try{
            ExportPackage exportPackage_save = new ExportPackage();
            exportPackage_save.setPackageName(exportPackage.getPackageName());
            exportPackage_save.setExportDate(exportPackage.getExportDate());
            exportPackage_save.setNote(exportPackage.getNote());
            exportPackage_save.setIdSender(new ObjectId(exportPackage.getSender().get_id()));
            exportPackage_save.setCustomerId(new ObjectId(exportPackage.getCustomer().getId()));
            exportPackage_save.setStatusDone(exportPackage.isStatusDone());
            List<ProductResponse> productResponseList = exportPackage.getListProducts();
            List<ObjectId> productIdList = new ArrayList<>();
            for (ProductResponse productResponse : productResponseList){
                productIdList.add(new ObjectId(productResponse.getId()));
            }
            exportPackage_save.setListProducts(productIdList);
            exportPackageRepos.save(exportPackage_save);


        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding export package", e);
        }
        return null;
    }

    @Override
    public ResponseEntity<ExportPackage> updateExportPackage(String _id, ExportPackageResponse exportPackage) {
        try {
            Optional<ExportPackage> existingPackage = exportPackageRepos.findById(_id);
            if (existingPackage.isPresent()) {
                ExportPackage exportPackage_save = existingPackage.get();
                exportPackage_save.setPackageName(exportPackage.getPackageName());
                exportPackage_save.setExportDate(exportPackage.getExportDate());
                exportPackage_save.setDeliveryMethod(exportPackage.getDeliveryMethod());
                exportPackage_save.setNote(exportPackage.getNote());
                exportPackage_save.setIdSender(new ObjectId(exportPackage.getSender().get_id()));
                exportPackage_save.setCustomerId(new ObjectId(exportPackage.getCustomer().getId()));
                exportPackage_save.setStatusDone(exportPackage.isStatusDone());
                List<ProductResponse> productResponseList = exportPackage.getListProducts();
                List<ObjectId> productIdList = new ArrayList<>();
                for (ProductResponse productResponse : productResponseList){
                    productIdList.add(new ObjectId(productResponse.getId()));
                }
                exportPackage_save.setListProducts(productIdList);
                exportPackageRepos.save(exportPackage_save);
                return ResponseEntity.ok(exportPackage_save);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating export package", e);
        }
    }

    @Override
    public ResponseEntity<ExportPackage> deleteExportPackage(String id) {
        try {
            exportPackageRepos.deleteById(id);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error deleting export package", e);
        }
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

    @Override
    public List<ExportPackageResponse> getAllPendingPackages() {
        try {
            List<ExportPackageResponse> pendingList = new ArrayList<>();
            for (ExportPackageResponse exportPackage : getAllExportPackages()) {
                if (!exportPackage.isStatusDone()) {
                    pendingList.add(exportPackage);
                }
            }
            return pendingList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting export packages", e);
        }
    }

    @Override
    public List<ExportPackageResponse> getAllDonePackages() {
        try {
            List<ExportPackageResponse> doneList = new ArrayList<>();
            for (ExportPackageResponse exportPackage : getAllExportPackages()) {
                if (exportPackage.isStatusDone()) {
                    doneList.add(exportPackage);
                }
            }
            return doneList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting export packages", e);
        }
    }
}
