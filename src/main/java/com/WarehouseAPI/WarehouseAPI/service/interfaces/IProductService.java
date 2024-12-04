package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.dto.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.dto.StorageLocationSummary;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


public interface IProductService {
    public ResponseEntity<ProductResponse> addProduct(ProductResponse productResponse);
    public ResponseEntity<String> updateProduct(String _id, ProductResponse updatedProduct);
    public ResponseEntity<String> deleteProduct(String idProduct);
    public ProductResponse getProduct(String _id);
    public List<ProductResponse> getSortedProductAscending(String props);
    public List<ProductResponse> getSortedProductDescending(String props);

    List<ProductResponse> getFilteredProducts(Map<String, String> filters);
    public ProductResponse getPendingProduct(String _id);
    public List<ProductResponse> getSearchedProducts(String props, String value);
    public List<ProductResponse> getAllProducts();
    public List<ProductResponse> getProductsByLastUpdatedDateRange(String startDay, String endDay);
    public List<ProductResponse> getProductsByMonth(int year, int month);
    public List<StorageLocationSummary> getStockSummaryByLocation();
}
