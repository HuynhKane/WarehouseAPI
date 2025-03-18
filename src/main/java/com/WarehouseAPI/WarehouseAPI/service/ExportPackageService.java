package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.dto.*;
import com.WarehouseAPI.WarehouseAPI.exception.InsufficientStockException;
import com.WarehouseAPI.WarehouseAPI.model.*;
import com.WarehouseAPI.WarehouseAPI.repository.ExportPackageRepos;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IExportPackage;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExportPackageService implements IExportPackage {


    @Autowired
    private final ExportPackageRepos exportPackageRepos;
    private final MongoTemplate mongoTemplate;
    private final ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;

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
                else {
                    productFromDb.setQuantity(productFromDb.getQuantity() - product.getQuantity());
                    if (Objects.equals(productFromDb.getQuantity(), 0)){
                        productFromDb.setInStock(false);
                    }
                    productRepository.save(productFromDb);
                    exportPackage.setStatusDone("APPROVED");
                    exportPackage.setIdSender(new ObjectId("67276a79a0b1c2534dca6e61"));
                    exportPackageRepos.save(exportPackage);
                }
            }
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
            ExportPackageResponse exportPackageResponse = new ExportPackageResponse();
            exportPackageResponse.setId(_id);
            Optional<ExportPackage> exportPackageOpt = exportPackageRepos.findById(_id);
            if (exportPackageOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Export package not found");

            }
            BigDecimal totalPrice = BigDecimal.valueOf(0);
            exportPackageResponse.setPackageName(exportPackageOpt.get().getPackageName());
            exportPackageResponse.setExportDate(exportPackageOpt.get().getExportDate());
            exportPackageResponse.setNote(exportPackageOpt.get().getNote());
            exportPackageResponse.setDeliveryMethod(exportPackageOpt.get().getDeliveryMethod());
            List<ProductResponseQuantity> productResponseQuantityList = new ArrayList<>();
            for (ProductWithQuantity product : exportPackageOpt.get().getListProducts()) {
                Optional<Product> productOpt = productRepository.findById(String.valueOf(product.getProductId()));
                if (productOpt.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + product.getProductId());
                }
                Product productFromDb = productOpt.get();
                productFromDb.setQuantity(productFromDb.getQuantity() - product.getQuantity());
                if (Objects.equals(productFromDb.getQuantity(), 0)){
                    productFromDb.setInStock(false);
                }
                totalPrice = totalPrice.add(productFromDb.getSellingPrice().multiply(BigDecimal.valueOf(product.getQuantity())));
                productRepository.save(productFromDb);
                ProductResponseQuantity productResponseQuantity = new ProductResponseQuantity();
                productResponseQuantity.setProduct(productService.getProduct(String.valueOf(product.getProductId())));
                productResponseQuantity.setQuantity(product.getQuantity());
                productResponseQuantityList.add(productResponseQuantity);
            }
            exportPackageResponse.setListProducts(productResponseQuantityList);
            User user = userService.getUser(String.valueOf(exportPackageOpt.get().getIdSender()));
            exportPackageResponse.setSender(user);
            Customer customer = customerService.getCustomer(String.valueOf(exportPackageOpt.get().getCustomerId()));
            exportPackageResponse.setCustomer(customer);
            exportPackageResponse.setTotalSellingPrice(totalPrice);
            exportPackageResponse.setStatusDone(exportPackageOpt.get().getStatusDone());



            return exportPackageResponse;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting import packages", e);
        }
    }

    @Override
    public List<ExportPackageResponse> getAllExportPackages() {
        try {
            return exportPackageRepos.findAll().stream()
                    .map(exportPackage -> getExportPackage(exportPackage.getId()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting export packages", e);
        }
    }



    @Override
    public List<ExportPackageResponse> getAllPendingPackages() {
        try {
            return getAllExportPackages().stream()
                    .filter(exportPackage -> Objects.equals(exportPackage.getStatusDone(),"PENDING"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting export packages", e);
        }
    }

    @Override
    public List<ExportPackageResponse> getAllDonePackages() {
        try {
            return getAllExportPackages().stream()
                    .filter(exportPackage -> Objects.equals(exportPackage.getStatusDone(),"APPROVED"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting export packages", e);
        }
    }

    @Override
    public ResponseEntity<ExportPackage> updateInforPendingPackage(String _id, ExportPackageResponse exportPackageResponse) {
        try {
            ExportPackage exportPackage = exportPackageRepos.findById(_id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Export Package not found"));

            ////////
            exportPackage.setPackageName(exportPackageResponse.getPackageName());
            exportPackage.setNote(exportPackageResponse.getNote());
            exportPackage.setDeliveryMethod(exportPackageResponse.getDeliveryMethod());
            exportPackage.setIdSender(new ObjectId(exportPackageResponse.getSender().get_id()));
            exportPackage.setCustomerId(new ObjectId(exportPackageResponse.getCustomer().getId()));
            /////////
            Map<ObjectId, ProductResponseQuantity> productResponseMap = exportPackageResponse.getListProducts().stream()
                    .collect(Collectors.toMap(product -> new ObjectId(product.getProduct().getId()), product -> product));
            List<Product> updatedProducts = new ArrayList<>();
            for (ProductWithQuantity productWithQuantity : exportPackage.getListProducts()) {
                productRepository.findById(productWithQuantity.getProductId().toHexString()).ifPresent(pendingProduct -> {
                    ProductResponseQuantity productResponse = productResponseMap.get(productWithQuantity.getProductId());
                    ProductResponse productResponse1 = productResponse.getProduct();
                    if (productResponse1 != null) {
                        pendingProduct.setProductName(productResponse1.getProductName());
                        pendingProduct.setGenreId(new ObjectId(productResponse1.getGenre().get_id()));
                        pendingProduct.setQuantity(productResponse1.getQuantity());
                        pendingProduct.setDescription(productResponse1.getDescription());
                        pendingProduct.setImportPrice(productResponse1.getImportPrice());
                        pendingProduct.setSellingPrice(productResponse1.getSellingPrice());
                        pendingProduct.setSupplierId(new ObjectId(productResponse1.getSupplier().get_id()));
                        updatedProducts.add(pendingProduct);
                    }
                });
            }

            // Save all updated products at once
            if (!updatedProducts.isEmpty()) {
                productRepository.saveAll(updatedProducts);
            }

            // Save updated import package
            exportPackageRepos.save(exportPackage);
            return ResponseEntity.ok(exportPackage);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating import package", e);
        }
    }
}
