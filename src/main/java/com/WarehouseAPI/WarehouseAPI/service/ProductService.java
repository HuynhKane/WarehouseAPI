package com.WarehouseAPI.WarehouseAPI.service;
import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.model.StorageLocation;
import com.WarehouseAPI.WarehouseAPI.model.response.ProductInStorageLocation;
import com.WarehouseAPI.WarehouseAPI.model.response.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.model.response.StorageLocationSummary;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IProductService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService  implements IProductService {
    @Autowired
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    private StorageLocationService storageLocationService;

    public ProductService(ProductRepository productRepository, MongoTemplate mongoTemplate){
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ResponseEntity<String> addProduct(ProductResponse productResponse) {
        try {
            Product product = new Product();
            product.setProductName(productResponse.getProductName());
            product.setGenreId(new ObjectId(productResponse.getGenre().get_id()));
            product.setQuantity(productResponse.getQuantity());
            product.setDescription(productResponse.getDescription());
            product.setImportPrice(productResponse.getImportPrice());
            product.setSellingPrice(productResponse.getSellingPrice());
            product.setSupplierId(new ObjectId(productResponse.getSupplier().get_id()));
            product.setStorageLocationId(new ObjectId(productResponse.getStorageLocation().get_id()));
            product.setImage(productResponse.getImage());
            product.setLastUpdated(productResponse.getLastUpdated());
            product.setInStock(productResponse.isInStock());
            productRepository.save(product);
            return ResponseEntity.ok("Add Successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding product", e);
        }
    }

    @Override
    public ResponseEntity<String> updateProduct(String _id, ProductResponse updatedProduct) {
        try {
            Optional<Product> existingProductOpt = productRepository.findById(_id);
            if (existingProductOpt.isPresent()) {
                Product existingProduct = existingProductOpt.get();
                if (updatedProduct.getProductName() != null) existingProduct.setProductName(updatedProduct.getProductName());
                if (updatedProduct.getGenre().get_id() != null) existingProduct.setGenreId(new ObjectId(updatedProduct.getGenre().get_id()));
                if (updatedProduct.getQuantity() >= 0) existingProduct.setQuantity(updatedProduct.getQuantity());
                if (updatedProduct.getDescription() != null) existingProduct.setDescription(updatedProduct.getDescription());
                if (updatedProduct.getImportPrice() != null) existingProduct.setImportPrice(updatedProduct.getImportPrice());
                if (updatedProduct.getSellingPrice() != null) existingProduct.setSellingPrice(updatedProduct.getSellingPrice());
                if (updatedProduct.getSupplier().get_id() != null) existingProduct.setSupplierId(new ObjectId(updatedProduct.getSupplier().get_id()));
                if (updatedProduct.getStorageLocation().get_id() != null) existingProduct.setStorageLocationId(new ObjectId(updatedProduct.getStorageLocation().get_id()));
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
                    Aggregation.match(Criteria.where("_id").is(new ObjectId(_id))),
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
    @Override
    public List<ProductResponse> getProductsByLastUpdatedDateRange(String startDay, String endDay) {
        try {

            LocalDate startDate = LocalDate.parse(startDay);
            LocalDate endDate = LocalDate.parse(endDay);

            Instant startInstant = startDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
            Instant endInstant = endDate.atStartOfDay(ZoneId.of("UTC")).plusDays(1).toInstant();
            System.out.println(startInstant);
            System.out.println(endInstant);

            Criteria criteria = Criteria.where("lastUpdated")
                    .gte(startInstant)
                    .lt(endInstant);

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
            System.out.println(results.getMappedResults());
            return results.getMappedResults();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting products by date range", e);
        }
    }
    @Override
    public List<ProductResponse> getProductsByMonth(int year, int month) {
        try {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

            Instant startInstant = startDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
            Instant endInstant = endDate.atStartOfDay(ZoneId.of("UTC")).plusDays(1).toInstant();

            Criteria criteria = Criteria.where("lastUpdated")
                    .gte(startInstant)
                    .lt(endInstant);

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
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching products by month", e);
        }
    }

    public List<StorageLocationSummary> getStockSummaryByLocation() {
        return productRepository.getStockSummaryByLocation();
    }
}




