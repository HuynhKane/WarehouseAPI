package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.dto.ProductResponse;
import com.WarehouseAPI.WarehouseAPI.model.Customer;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.ICustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {


    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomerDetails() {
        return customerService.getAllCustomer();
    }

    @GetMapping("/{id}")
    public Customer getCustomerDetails(@PathVariable String id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public String addCustomerDetails(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
        return "Customer was created successfully";
    }

    @GetMapping("/search")
    public List<Customer> getSearchedProductsDetails(@RequestParam("props") String props, @RequestParam("value") String value){
        return customerService.getSearchedCustomers(props, value);
    }

    @PutMapping("/{id}")
    public String updateCustomerDetails(@PathVariable String id, @RequestBody Customer updatedCustomer) {
        customerService.updateCustomer(id, updatedCustomer);
        return "Customer was updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteCustomerDetails(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return "Customer was deleted successfully";
    }
}