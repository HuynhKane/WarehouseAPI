package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Customer;
import com.WarehouseAPI.WarehouseAPI.repository.CustomerRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    CustomerRepository customerRepository;
    private final MongoTemplate mongoTemplate;

    public CustomerService(CustomerRepository customerRepository, MongoTemplate mongoTemplate){
        this.customerRepository = customerRepository;
        this.mongoTemplate = mongoTemplate;
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
    public List<Customer> getSearchedCustomers(String props, String value) {
        try {
            Criteria criteria = new Criteria();
            if ("customerName".equals(props)) {
                criteria = Criteria.where("customerName").regex(value, "i");
            } else if ("id".equals(props)) {
                criteria = Criteria.where("_id").regex(value, "i");

            } else {
                return null;
            }
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(criteria)
            );
            AggregationResults<Customer> results = mongoTemplate.aggregate(
                    aggregation, "customer", Customer.class);
            return results.getMappedResults();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching products", e);
        }
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }
}
