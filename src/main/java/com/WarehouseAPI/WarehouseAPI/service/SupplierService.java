package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Supplier;
import com.WarehouseAPI.WarehouseAPI.repository.SupplierRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class SupplierService implements ISupplierService {

    @Autowired
    SupplierRepository supplierRepository;
    private final MongoTemplate mongoTemplate;
    public  SupplierService(SupplierRepository supplierRepository, MongoTemplate mongoTemplate){
        this.supplierRepository = supplierRepository;
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public String addSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
        return "Add supplier, done";
    }

    @Override
    public String updateSupplier(String _id, Supplier supplier) {
        supplierRepository.save(supplier);
        return "update supplier, done";
    }
    @Override
    public List<Supplier> findSupplierByName(String value){
        try {
            Criteria criteria = Criteria.where("name").regex(value, "i");
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.match(criteria));

            AggregationResults<Supplier> results = mongoTemplate.aggregate(
                    aggregation, "supplier", Supplier.class);
            return results.getMappedResults();
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching supplier", e);
        }

    }
    @Override
    public String deleteSupplier(String _id) {
        supplierRepository.deleteById(_id);
        return "delete supplier, done";
    }

    @Override
    public Supplier getSupplier(String _id) {
        if (supplierRepository.findById(_id).isEmpty())
            return null;
        return supplierRepository.findById(_id).get();
    }

    @Override
    public List<Supplier> getAllSupplier() {
        return supplierRepository.findAll();
    }
}
