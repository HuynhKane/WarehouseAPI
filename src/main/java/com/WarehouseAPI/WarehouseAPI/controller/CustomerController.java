package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.Customer;
import com.WarehouseAPI.WarehouseAPI.service.ICustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    ICustomerService iCustomerService;

    public CustomerController(ICustomerService iCustomerService){
        this.iCustomerService = iCustomerService;
    }

    @GetMapping("/all")
    public List<Customer> getAllCustomerDetails(){
        return iCustomerService.getAllCustomer();
    }

    @GetMapping("/{id}/get")
    public Customer getCustomerDetails(@PathVariable("_id") String _id){
        return iCustomerService.getCustomer(_id);
    }

    @PostMapping("/add")
    public String addCustomerDetails(@RequestBody Customer customer){
        iCustomerService.addCustomer(customer);
        return "Customer was created successfully";
    }

    @PutMapping("/{_id}/update")
    public String updateCustomerDetails(@PathVariable("_id") String _id, @RequestBody Customer updatedCustomer){
        iCustomerService.updateCustomer(_id, updatedCustomer);
        return "Customer was updated successfully";
    }

    @DeleteMapping("/{_id}/delete")
    public String deleteCustomerDetails(@PathVariable("_id") String _id){
        iCustomerService.deleteCustomer(_id);
        return "Customer was deleted successfully";
    }
}
