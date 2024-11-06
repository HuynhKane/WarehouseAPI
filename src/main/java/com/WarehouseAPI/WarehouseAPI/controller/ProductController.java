package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.service.IProductService;
import com.WarehouseAPI.WarehouseAPI.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/product")
public class ProductController {

    IProductService iProductService;

    public ProductController(IProductService iProductService){
        this.iProductService = iProductService;
    }

    @GetMapping("/all")
    public List<Product> getAllProductDetails(){
        return iProductService.getAllProducts();
    }

    @GetMapping("/{_id}/get")
    public Product getProductDetails(@PathVariable("_id") String _id){
        return iProductService.getProduct(_id);
    }

    @PostMapping("/add")
    public String addProductDetails(@RequestBody Product product){
        iProductService.addProduct(product);
        return "Product was created successfully";
    }

    @PutMapping("/{_id}/update")
    public String updateProductDetails(@PathVariable("_id") String _id, @RequestBody Product updatedProduct){
        iProductService.updateProduct(_id, updatedProduct);
        return "Product was updated successfully";
    }

    @DeleteMapping("/{_id}/delete")
    public String deleteProductDetails(@PathVariable("_id") String _id){
        iProductService.deleteProduct(_id);
        return "Product was deleted successfully";
    }
}
