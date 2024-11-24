package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Genre;
import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.model.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
import lombok.extern.java.Log;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.Console;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService  implements IProductService{
    @Autowired
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    public ProductService(ProductRepository productRepository, MongoTemplate mongoTemplate){
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ResponseEntity<String> addProduct(Product product) {
        try {
            productRepository.save(product);
            return ResponseEntity.ok("Add Successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding product", e);
        }
    }

    @Override
    public ResponseEntity<String> updateProduct(String _id, Product updatedProduct) {
        try {
            Optional<Product> existingProductOpt = productRepository.findById(_id);
            if (existingProductOpt.isPresent()) {
                Product existingProduct = existingProductOpt.get();
                if (updatedProduct.getProductName() != null) existingProduct.setProductName(updatedProduct.getProductName());
                if (updatedProduct.getGenreId() != null) existingProduct.setGenreId(updatedProduct.getGenreId());
                if (updatedProduct.getQuantity() >= 0) existingProduct.setQuantity(updatedProduct.getQuantity());
                if (updatedProduct.getDescription() != null) existingProduct.setDescription(updatedProduct.getDescription());
                if (updatedProduct.getImportPrice() != null) existingProduct.setImportPrice(updatedProduct.getImportPrice());
                if (updatedProduct.getSellingPrice() != null) existingProduct.setSellingPrice(updatedProduct.getSellingPrice());
                if (updatedProduct.getSupplierId() != null) existingProduct.setSupplierId(updatedProduct.getSupplierId());
                if (updatedProduct.getStorageLocationId() != null) existingProduct.setStorageLocationId(updatedProduct.getStorageLocationId());
                if (updatedProduct.getLastUpdated() != null) existingProduct.setLastUpdated(updatedProduct.getLastUpdated());
                if (updatedProduct.getImage() != null) existingProduct.setImage(updatedProduct.getImage());
                productRepository.save(existingProduct);
                return ResponseEntity.ok("Update Successful");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating product", e);
        }
    }


    @Override
    public ResponseEntity<String> deleteProduct(String idProduct) {
        try {
            productRepository.deleteById(idProduct);
            return ResponseEntity.ok("Delete Successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting product", e);
        }
    }



    @Override
    public ProductResponse getProduct(String _id) {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("_id").is(new ObjectId(_id))), // Match by ObjectId
                    Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                    Aggregation.lookup("genre", "genreId", "_id", "genre"),
                    Aggregation.lookup("storageLocation", "storageLocationId", "_id", "storageLocation"),
                    Aggregation.unwind("supplier", true),
                    Aggregation.unwind("storageLocation", true),
                    Aggregation.unwind("genre", true)
            );
            AggregationResults<ProductResponse> result = mongoTemplate.aggregate(
                    aggregation, "product", ProductResponse.class);
            return result.getUniqueMappedResult();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting product", e);
        }
    }
    @Override
    public List<ProductResponse> getAllProducts() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                Aggregation.lookup("genre", "genreId", "_id", "genre"),
                Aggregation.lookup("storageLocation", "storageLocationId", "_id", "storageLocation"),
                Aggregation.unwind("supplier", true),
                Aggregation.unwind("storageLocation", true),
                Aggregation.unwind("genre", true)
        );
        AggregationResults<ProductResponse> results = mongoTemplate.aggregate(
                aggregation, "product", ProductResponse.class);
        return results.getMappedResults();
    }
    @Override
    public List<ProductResponse> getSortedProductAscending(String props) {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                    Aggregation.lookup("genre", "genreId", "_id", "genre"),
                    Aggregation.lookup("storageLocation", "storageLocationId", "_id", "storageLocation"),
                    Aggregation.unwind("supplier", true),
                    Aggregation.unwind("storageLocation", true),
                    Aggregation.unwind("genre", true),
                    Aggregation.sort(Sort.Direction.ASC, props)
            );
            AggregationResults<ProductResponse> results = mongoTemplate.aggregate(
                    aggregation, "product", ProductResponse.class);
            return results.getMappedResults();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sorting products (ascending)", e);
        }
    }

    @Override
    public List<ProductResponse> getSortedProductDescending(String props) {
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                    Aggregation.lookup("genre", "genreId", "_id", "genre"),
                    Aggregation.lookup("storageLocation", "storageLocationId", "_id", "storageLocation"),
                    Aggregation.unwind("supplier", true),
                    Aggregation.unwind("storageLocation", true),
                    Aggregation.unwind("genre", true),
                    Aggregation.sort(Sort.Direction.DESC, props)
            );
            AggregationResults<ProductResponse> results = mongoTemplate.aggregate(
                    aggregation, "product", ProductResponse.class);
            return results.getMappedResults();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sorting products (descending)", e);
        }
    }

    @Override
    public List<ProductResponse> getFilteredProducts(String props, String value) {
        try{
            Criteria criteria = new Criteria();
            if("supplierId".equals(props)){
                criteria = Criteria.where("supplierId").is(value);
            }else if("isInStock".equals(props)){
                criteria = Criteria.where("isInStock").is(Boolean.parseBoolean(value));
            }else if("storageLocationId".equals(props)){
                criteria = Criteria.where("storageLocationId").is(value);
            }else{
                return null;
            }
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(criteria),
                    Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                    Aggregation.lookup("genre", "genreId", "_id", "genre"),
                    Aggregation.lookup("storageLocation", "storageLocationId", "_id", "storageLocation"),
                    Aggregation.unwind("supplier", true),
                    Aggregation.unwind("storageLocation", true),
                    Aggregation.unwind("genre", true)
            );
            AggregationResults<ProductResponse> results = mongoTemplate.aggregate(
                    aggregation, "product", ProductResponse.class);
            return results.getMappedResults();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error filtering products", e);
        }
    }

    @Override
    public List<ProductResponse> getSearchedProducts(String props, String value) {
        try {
            Criteria criteria = new Criteria();
            if ("productName".equals(props)) {
                criteria = Criteria.where("productName").regex(value, "i");
            } else {
                return null;
            }
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(criteria),
                    Aggregation.lookup("supplier", "supplierId", "_id", "supplier"),
                    Aggregation.lookup("genre", "genreId", "_id", "genre"),
                    Aggregation.lookup("storageLocation", "storageLocationId", "_id", "storageLocation"),
                    Aggregation.unwind("supplier", true),
                    Aggregation.unwind("storageLocation", true),
                    Aggregation.unwind("genre", true)
            );
            AggregationResults<ProductResponse> results = mongoTemplate.aggregate(
                    aggregation, "product", ProductResponse.class);
            return results.getMappedResults();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching products", e);
        }
    }



}




