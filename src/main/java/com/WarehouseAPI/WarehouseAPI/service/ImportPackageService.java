package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.*;
import com.WarehouseAPI.WarehouseAPI.dto.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.dto.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.repository.ImportPackageRepos;
import com.WarehouseAPI.WarehouseAPI.repository.PendingProductRepos;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ImportPackageService implements IImportPackage {


    @Autowired
    private final ImportPackageRepos importPackageRepos;
    private final MongoTemplate mongoTemplate;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private NotificationService notificationService;
    private final PendingProductRepos pendingProductRepository;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public ImportPackageService(ImportPackageRepos importPackageRepos, MongoTemplate mongoTemplate, ProductService productService, ProductRepository productRepository, NotificationService notificationService, PendingProductRepos pendingProductRepository) {
        this.importPackageRepos = importPackageRepos;
        this.mongoTemplate = mongoTemplate;
        this.productService = productService;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
        this.pendingProductRepository = pendingProductRepository;
    }

    public ResponseEntity<ImportPackageResponse> addPendingImportPackage(ImportPackageResponse importPackage) {
        try {
            executorService.submit(() -> {
                Notification notification = new Notification();
                notification.setTitle("New Package Added");
                notification.setDescription("Package: " + importPackage.getPackageName() + " has been added successfully.");
                notification.setType("INFO");
                notification.setTimestamp(new Date());
                System.out.println(notification);
                notificationService.sendNotification(notification);
            });

            executorService.submit(() -> {
                ImportPackage importPackage_save = new ImportPackage();
                importPackage_save.setPackageName(importPackage.getPackageName());
                importPackage_save.setImportDate(importPackage.getImportDate());
                importPackage_save.setNote(importPackage.getNote());
                importPackage_save.setIdReceiver(new ObjectId(importPackage.getReceiver().get_id()));
                importPackage_save.setStatusDone("PENDING");
                importPackage_save.setImportDate(LocalDateTime.now());
                List<ProductResponse> productResponseList = importPackage.getListProducts();
                List<PendingProduct> listPendingProducts = new ArrayList<>();
                List<ObjectId> listIdProducts = new ArrayList<>();
                for (ProductResponse productResponse : productResponseList) {

                    PendingProduct pendingProduct = new PendingProduct();
                    pendingProduct.setProductName(productResponse.getProductName());
                    pendingProduct.setGenreId(new ObjectId(productResponse.getGenre().get_id()));
                    pendingProduct.setQuantity(productResponse.getQuantity());
                    pendingProduct.setDescription(productResponse.getDescription());
                    pendingProduct.setImportPrice(productResponse.getImportPrice());
                    pendingProduct.setSellingPrice(productResponse.getSellingPrice());
                    pendingProduct.setSupplierId(new ObjectId(productResponse.getSupplier().get_id()));
                    pendingProductRepository.save(pendingProduct);
                    listPendingProducts.add(pendingProduct);
                }

                for (PendingProduct pendingProduct : listPendingProducts) {
                    System.out.println(pendingProduct.getId());
                    listIdProducts.add(new ObjectId(pendingProduct.getId()));
                }
                importPackage_save.setListProducts(listIdProducts);
                importPackageRepos.save(importPackage_save);
                System.out.println(importPackage_save);
            });

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding import package", e);
        }

        return null;
    }
    @Override
    public ResponseEntity<ImportPackage> updateDeclineImportPackage(String _id){
        try {
            Optional<ImportPackage> existingPackage = importPackageRepos.findById(_id);
            if (existingPackage.isPresent()) {
                ImportPackage importPackage_save = existingPackage.get();
                importPackage_save.setStatusDone("DECLINE");
                importPackageRepos.save(importPackage_save);
                return ResponseEntity.ok(importPackage_save);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating import package", e);
        }
    }

    public ResponseEntity<ImportPackage> updateProductImportPackage(String id, Map<String, String> storageLocationIds) {
        try {
            Optional<ImportPackage> importPackageOpt = importPackageRepos.findById(id);
            if (importPackageOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            ImportPackage importPackage = importPackageOpt.get();
            for (ObjectId productId : importPackage.getListProducts()) {
                for (Map.Entry<String, String> entry : storageLocationIds.entrySet()) {
                    String productIdString = entry.getKey();
                    String storageLocationId = entry.getValue();
                    if (Objects.equals(productId.toString(), productIdString)) {
                        try {
                            Optional<Product> productOpt = productRepository.findById(productIdString);
                            if (productOpt.isPresent()) {
                                Product product = productOpt.get();
                                try {
                                    product.setStorageLocationId(new ObjectId(storageLocationId));
                                    product.setInStock(true);
                                    productRepository.save(product);
                                } catch (Exception e) {
                                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                            .body(null);
                                }
                            } else {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body(null);
                            }
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body(null);
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(importPackage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @Override
    public ResponseEntity<ImportPackage> updateImportPackage(String _id) {
        try {
            Optional<ImportPackage> existingPackage = importPackageRepos.findById(_id);
            if (existingPackage.isPresent()) {
                existingPackage.get().setStatusDone("APPROVED");
                List<ObjectId> productIdList = new ArrayList<>();
                for (ObjectId pendingProductId : existingPackage.get().getListProducts()) {

                    String approvedProductId = approvePendingProduct(pendingProductId);
                    productIdList.add(new ObjectId(approvedProductId));
                }
                existingPackage.get().setListProducts(productIdList);
                importPackageRepos.save(existingPackage.get());
                return ResponseEntity.ok(existingPackage.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating import package", e);
        }
    }

    public String approvePendingProduct(ObjectId pendingProductId) {
        String pendingProductIdstr = pendingProductId.toHexString();
        Optional<PendingProduct> pendingProductOpt = pendingProductRepository.findById(pendingProductIdstr);
        if (pendingProductOpt.isPresent()) {
            PendingProduct pendingProduct = pendingProductOpt.get();
            Product product = new Product();
            product.setProductName(pendingProduct.getProductName());
            product.setGenreId(pendingProduct.getGenreId());
            product.setQuantity(pendingProduct.getQuantity());
            product.setDescription(pendingProduct.getDescription());
            product.setImportPrice(pendingProduct.getImportPrice());
            product.setSellingPrice(pendingProduct.getSellingPrice());
            product.setSupplierId(pendingProduct.getSupplierId());
            product.setLastUpdated(new Date());
            product.setImage(pendingProduct.getImage());
            product.setStorageLocationId(null);
            product.setInStock(true);
            // Lưu vào collection Product
            product = productRepository.save(product);

            System.out.println("Approved and moved product: " + product.getProductName());
            return product.get_id(); // Trả về ID của sản phẩm mới (String)
        } else {
            throw new RuntimeException("Pending product not found with ID: " + pendingProductId);
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
                    Aggregation.lookup("product", "listProducts", "_id", "listProducts"),
                    Aggregation.unwind("receiver", true)

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
    public ImportPackageResponse getPendingImportPackage(String _id) {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("_id").is(new ObjectId(_id))),
                    Aggregation.lookup("user", "idReceiver", "_id", "receiver"),
                    Aggregation.lookup("pendingProduct", "listProducts", "_id", "listProducts"),
                    Aggregation.unwind("receiver", true)

            );
            AggregationResults<ImportPackageResponse> result = mongoTemplate.aggregate(
                    aggregation, "importPackage", ImportPackageResponse.class);

            ImportPackageResponse importPackage = result.getUniqueMappedResult();
            List<ProductResponse> listProducts = new ArrayList<>();
            for (ProductResponse product : importPackage.getListProducts()) {
                System.out.println(product);
                listProducts.add(productService.getPendingProduct(product.getId()));
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
                Aggregation.lookup("product", "listProducts", "_id", "listProducts"),
                Aggregation.unwind("receiver", true)

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
            List<ImportPackageResponse> importPackages = getAllImportPackages();
            List<ImportPackageResponse> pendingPackages = new ArrayList<>();
            for(ImportPackageResponse importPackageResponse: importPackages){
                if(Objects.equals(importPackageResponse.getStatusDone(), "PENDING")){

                    pendingPackages.add(getPendingImportPackage(importPackageResponse.getId()));
                }
            }
            return pendingPackages;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }

    @Override
    public List<ImportPackageResponse> getAllDonePackages() {
        try {
            List<ImportPackageResponse> doneList = new ArrayList<>();
            for (ImportPackageResponse importPackage : getAllImportPackages()) {
                if (Objects.equals(importPackage.getStatusDone(), "APPROVED")) {
                    doneList.add(importPackage);
                }
            }
            return doneList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }

}
