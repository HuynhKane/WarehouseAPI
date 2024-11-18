package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.service.IProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProductDetails() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductDetails(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @PostMapping
    public String addProductDetails(@RequestBody Product product) {
        productService.addProduct(product);
        return "Product added successfully";
    }
    @PostMapping("/addList")
    public String addProductsDetails(@RequestBody List<Product> products) {
        for (Product product : products) {
            productService.addProduct(product);
        }
        return "List product added successfully";
    }

    @PutMapping("/{id}")
    public String updateProductDetails(@PathVariable String id, @RequestBody Product updatedProduct) {
        productService.updateProduct(id, updatedProduct);
        return "Product updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteProductDetails(@PathVariable String id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
}