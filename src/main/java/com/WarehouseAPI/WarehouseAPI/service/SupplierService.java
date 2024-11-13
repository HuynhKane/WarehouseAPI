package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Supplier;
import com.WarehouseAPI.WarehouseAPI.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SupplierService implements ISupplierService{

    @Autowired
    SupplierRepository supplierRepository;

    public  SupplierService(SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;
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
