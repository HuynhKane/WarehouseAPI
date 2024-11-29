package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.ImportPackage;
import com.WarehouseAPI.WarehouseAPI.dto.ImportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.dto.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.model.Notification;
import com.WarehouseAPI.WarehouseAPI.model.PendingProduct;
import com.WarehouseAPI.WarehouseAPI.model.Product;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            // Create and start two threads for the actions
            executorService.submit(() -> {
                // Task 1: Notification creation and sending
                Notification notification = new Notification();
                notification.setTitle("New Package Added");
                notification.setDescription("Package: " + importPackage.getPackageName() + " has been added successfully.");
                notification.setType("INFO");
                notification.setTimestamp(new Date());
                System.out.println(notification);
                notificationService.sendNotification(notification);
            });

            executorService.submit(() -> {
                // Task 2: Save import package and process products
                ImportPackage importPackage_save = new ImportPackage();
                importPackage_save.setPackageName(importPackage.getPackageName());
                importPackage_save.setImportDate(importPackage.getImportDate());
                importPackage_save.setNote(importPackage.getNote());
                importPackage_save.setIdReceiver(new ObjectId(importPackage.getReceiver().get_id()));
                importPackage_save.setStatusDone("PENDING");
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
                    listIdProducts.add(pendingProduct.getId());
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
    public ResponseEntity<ImportPackageResponse> updateImportPackage(String _id, ImportPackageResponse importPackage) {
        try {
            Optional<ImportPackage> existingPackage = importPackageRepos.findById(_id);
            if (existingPackage.isPresent()) {
                ImportPackage importPackage_save = existingPackage.get();
                importPackage_save.setPackageName(importPackage.getPackageName());
                importPackage_save.setImportDate(importPackage.getImportDate());
                importPackage_save.setNote(importPackage.getNote());
                importPackage_save.setIdReceiver(new ObjectId(importPackage.getReceiver().get_id()));
                importPackage_save.setStatusDone(importPackage.getStatusDone());
                List<ProductResponse> productResponseList = importPackage.getListProducts();
                List<ObjectId> productIdList = new ArrayList<>();
                // Duyệt qua danh sách sản phẩm và gọi phương thức approvePendingProduct để duyệt
                for (ProductResponse productResponse : productResponseList) {
                    Optional<PendingProduct> pendingProduct = pendingProductRepository.findById(productResponse.getId());
                    String approvedProductId = approvePendingProduct(productResponse.getId(), productResponse.getStorageLocation().get_id(), productResponse.isInStock());
                    productIdList.add(new ObjectId(approvedProductId)); // Thêm ID sản phẩm đã duyệt vào danh sách
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

    public String approvePendingProduct(String pendingProductId, String storageLocationId, boolean inStock) {
        Optional<PendingProduct> pendingProductOpt = pendingProductRepository.findById(pendingProductId);
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
            product.setInStock(inStock);
            product.setStorageLocationId(new ObjectId(storageLocationId));
            // Lưu vào collection Product
            product = productRepository.save(product);

            // Xóa sản phẩm Pending (nếu cần)
            // pendingProductRepository.delete(pendingProduct);

            System.out.println("Approved and moved product: " + product.getProductName());
            return product.get_id().toString(); // Trả về ID của sản phẩm mới (String)
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
            List<ImportPackageResponse> pendingList = new ArrayList<>();
            for (ImportPackageResponse importPackage : getAllImportPackages()) {
                if (Objects.equals(importPackage.getStatusDone(), "PENDING")) {
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
                if (Objects.equals(importPackage.getStatusDone(), "APPROVED")) {
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
        try {
            List<ImportPackageResponse> doneList = new ArrayList<>();
            for (ImportPackageResponse importPackage : getAllImportPackages()) {
                if (Objects.equals(importPackage.getStatusDone(), "CANCELLED")) {
                    doneList.add(importPackage);
                }
            }
            return doneList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }
}
