package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.model.Customer;

import java.util.List;

public interface ICustomerService {
    public String addCustomer(Customer customer);
    public String updateCustomer(String _id, Customer updatedCustomer);
    public String deleteCustomer(String idCustomer);
    public Customer getCustomer(String _id);

    public List<Customer> getAllCustomer();
}
