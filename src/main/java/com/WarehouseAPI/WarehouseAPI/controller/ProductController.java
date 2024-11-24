package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.model.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.model.Supplier;
import com.WarehouseAPI.WarehouseAPI.service.IProductService;
import com.WarehouseAPI.WarehouseAPI.service.ISupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final IProductService productService;
    public ProductController(IProductService productService) {
        this.productService = productService;

    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable String id) {
        try {
            ProductResponse product = productService.getProduct(id);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product not found with id: " + id);
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching product details", e);
        }
    }

    @GetMapping("/sort")
    public List<ProductResponse> getSortedProductDetails(@RequestParam("props") String props, @RequestParam("order") String order) {
        if("asc".equalsIgnoreCase(order)){
            return productService.getSortedProductAscending(props);
        } else if ("desc".equalsIgnoreCase(order)) {
            return productService.getSortedProductDescending(props);
        } else
        {
            throw new IllegalArgumentException("Invalid sort order. Use 'asc' or 'desc'.");
        }
    }
    @GetMapping("/filter")
    public List<ProductResponse> getFilteredProductsDetails(@RequestParam("props") String props, @RequestParam("value") String value){
        return productService.getFilteredProducts(props, value);
    }
    @GetMapping("/search")
    public List<ProductResponse> getSearchedProductsDetails(@RequestParam("props") String props, @RequestParam("value") String value){
        return productService.getSearchedProducts(props, value);
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