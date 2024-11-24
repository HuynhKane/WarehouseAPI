package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.model.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IProductService {
    public ResponseEntity<String> addProduct(Product product);
    public ResponseEntity<String> updateProduct(String _id, Product updatedProduct);
    public ResponseEntity<String> deleteProduct(String idProduct);
    public ProductResponse getProduct(String _id);
    public List<ProductResponse> getSortedProductAscending(String props);
    public List<ProductResponse> getSortedProductDescending(String props);
    public List<ProductResponse> getFilteredProducts(String props, String value);
    public List<ProductResponse> getSearchedProducts(String props, String value);
    public List<ProductResponse> getAllProducts();
}
