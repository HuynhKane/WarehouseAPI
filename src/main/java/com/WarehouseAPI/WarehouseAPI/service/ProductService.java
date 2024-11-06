package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.repository.ProductRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService  implements IProductService{
    @Autowired
    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public String addProduct(Product product) {
        productRepository.save(product);
        return "Add Successfully";
    }

    @Override
    public String updateProduct(String _id, Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(_id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setIdProduct(updatedProduct.getIdProduct());
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setGenreId(updatedProduct.getGenreId());
            existingProduct.setQuantity(updatedProduct.getQuantity());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setImportPrice(updatedProduct.getImportPrice());
            existingProduct.setSellingPrice(updatedProduct.getSellingPrice());
            existingProduct.setSupplierId(updatedProduct.getSupplierId());
            existingProduct.setInStock(updatedProduct.isInStock());
            existingProduct.setBarcode(updatedProduct.getBarcode());
            existingProduct.setStorageLocationId(updatedProduct.getStorageLocationId());
            existingProduct.setLastUpdated(updatedProduct.getLastUpdated());
            existingProduct.setImage(updatedProduct.getImage());
            productRepository.save(existingProduct);

            return "Update Successfully";
        } else {
            return "Product not found";
        }
    }

    @Override
    public String deleteProduct(String idProduct) {
        productRepository.deleteById(idProduct);
        return "Delete Successfully";
    }

    @Override
    public Product getProduct(String _id) {
        if (productRepository.findById(_id).isEmpty())
            return null;
        return productRepository.findById(_id).get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
