package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.model.response.ProductInStorageLocation;
import com.WarehouseAPI.WarehouseAPI.model.response.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface IProductService {
    public ResponseEntity<String> addProduct(ProductResponse productResponse);
    public ResponseEntity<String> updateProduct(String _id, ProductResponse updatedProduct);
    public ResponseEntity<String> deleteProduct(String idProduct);
    public ProductResponse getProduct(String _id);
    public List<ProductResponse> getSortedProductAscending(String props);
    public List<ProductResponse> getSortedProductDescending(String props);
    public List<ProductResponse> getFilteredProducts(String props, String value);
    public List<ProductResponse> getSearchedProducts(String props, String value);
    public List<ProductResponse> getAllProducts();
    public List<ProductResponse> getProductsByLastUpdatedDateRange(String startDay, String endDay);
    public List<ProductResponse> getProductsByMonth(int year, int month);
    public List<ProductInStorageLocation> getProductQuantityByStorageLocation();
}
