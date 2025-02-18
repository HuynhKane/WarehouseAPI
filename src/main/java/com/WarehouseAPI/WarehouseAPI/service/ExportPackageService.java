package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.dto.ProductWithQuantity;
import com.WarehouseAPI.WarehouseAPI.exception.InsufficientStockException;
import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;
import com.WarehouseAPI.WarehouseAPI.dto.ExportPackageResponse;
import com.WarehouseAPI.WarehouseAPI.dto.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.repository.ExportPackageRepos;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ExportPackageService implements IExportPackage {


    @Autowired
    private final ExportPackageRepos exportPackageRepos;
    private final MongoTemplate mongoTemplate;
    private final ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    public ExportPackageService(ExportPackageRepos exportPackageRepos, MongoTemplate mongoTemplate, ProductService productService) {
        this.exportPackageRepos = exportPackageRepos;
        this.mongoTemplate = mongoTemplate;
        this.productService = productService;
    }

    @Override
    public ResponseEntity<ExportPackage> addPendingExportPackage(ExportPackage exportPackage) {
        try{
            ExportPackage exportPackage_save = new ExportPackage();
            exportPackage_save.setPackageName(exportPackage.getPackageName());
            exportPackage_save.setExportDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            exportPackage_save.setNote(exportPackage.getNote());
            exportPackage_save.setDeliveryMethod("Standard");
            exportPackage_save.setIdSender(new ObjectId(String.valueOf(exportPackage.getIdSender())));
            exportPackage_save.setCustomerId(new ObjectId(String.valueOf(exportPackage.getCustomerId())));
            exportPackage_save.setStatusDone("PENDING");
            List<ProductWithQuantity> productResponseList = exportPackage.getListProducts();
            List<ProductWithQuantity> productIdList = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.valueOf(0);
            for (ProductWithQuantity productResponse : productResponseList) {
                ProductWithQuantity product = new ProductWithQuantity();
                product.setProductId(new ObjectId(String.valueOf(productResponse.getProductId())));
                Optional<Product> productOptional = productRepository.findById(String.valueOf(productResponse.getProductId()));

                if (productOptional.isPresent()) {
                    Product product1 = productOptional.get();
                    BigDecimal quantity = BigDecimal.valueOf(productResponse.getQuantity());
                    totalPrice = totalPrice.add(product1.getSellingPrice().multiply(quantity));
                }

                
                product.setQuantity(productResponse.getQuantity());
                productIdList.add(product);
            }
            exportPackage_save.setListProducts(productIdList);
            exportPackage_save.setTotalSellingPrice(totalPrice);
            exportPackageRepos.save(exportPackage_save);


        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding export package", e);
        }
        return null;
    }

    @Override
    public ResponseEntity<ExportPackage> approveExportPackage(String packageId) {
        try {
            Optional<ExportPackage> exportPackageOpt = exportPackageRepos.findById(packageId);
            if (exportPackageOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Export package not found");
            }
            ExportPackage exportPackage = exportPackageOpt.get();
            for (ProductWithQuantity product : exportPackage.getListProducts()) {
                Optional<Product> productOpt = productRepository.findById(String.valueOf(product.getProductId()));  // Fetch product details
                if (productOpt.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + product.getProductId());
                }
                Product productFromDb = productOpt.get();
                if (product.getQuantity() > productFromDb.getQuantity()) {
                    throw new InsufficientStockException(
                            "Insufficient stock for product: " + product.getProductId() +
                                    ". Available: " + productFromDb.getQuantity() +
                                    ", Requested: " + product.getQuantity()
                    );
                }
                productFromDb.setQuantity(productFromDb.getQuantity() - product.getQuantity());
                if (Objects.equals(productFromDb.getQuantity(), 0)){
                    productFromDb.setInStock(false);
                }
                productRepository.save(productFromDb);
            }
            exportPackage.setStatusDone("APPROVED");
            exportPackage.setIdSender(new ObjectId("67276a79a0b1c2534dca6e61"));

            exportPackageRepos.save(exportPackage);
            return ResponseEntity.ok(exportPackage);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error approving export package", e);
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
                    Aggregation.lookup("product", "listProducts.productId", "_id", "listProducts"),
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
                    Aggregation.lookup("product", "listProducts.productId", "_id", "listProducts"),
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
            List<ExportPackage> packages = new ArrayList<>();
            for (ExportPackageResponse exportPackage : getAllExportPackages()) {
                if (Objects.equals(exportPackage.getStatusDone(),"PENDING")){
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
                if (!Objects.equals(exportPackage.getStatusDone(),"PENDING")){
                    doneList.add(exportPackage);
                }
            }
            return doneList;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting export packages", e);
        }
    }
}
