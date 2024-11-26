package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Customer;
import com.WarehouseAPI.WarehouseAPI.repository.CustomerRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }
    @Override
    public String addCustomer(Customer customer) {
        customerRepository.save(customer);
        return "add customer, ok";
    }

    @Override
    public String updateCustomer(String _id, Customer updatedCustomer) {
        Optional<Customer> existCustomerOpt = customerRepository.findById(_id);
        if (existCustomerOpt.isPresent()){
            Customer customer = existCustomerOpt.get();
            customer.setEmail(updatedCustomer.getEmail());
            customer.setCustomerName(updatedCustomer.getCustomerName());
            customer.setAddress(updatedCustomer.getAddress());
            customerRepository.save(customer);
            return "update, ok";
        }
        return "update failed";
    }

    @Override
    public String deleteCustomer(String idCustomer) {
        customerRepository.deleteById(idCustomer);
        return "delete customer, ok";
    }

    @Override
    public Customer getCustomer(String _id) {
        if(customerRepository.findById(_id).isEmpty()){
            return null;
        }
        return customerRepository.findById(_id).get();
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }
}
