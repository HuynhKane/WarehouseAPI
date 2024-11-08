package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IProductService {
    public String addProduct(Product product);
    public String updateProduct(String _id, Product updatedProduct);
    public String deleteProduct(String idProduct);
    public Product getProduct(String _id);
    public List<Product> getAllProducts();
}
