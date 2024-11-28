package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.model.Supplier;

import java.util.List;

public interface ISupplierService {
    public String addSupplier(Supplier supplier);
    public String updateSupplier(String _id, Supplier supplier);

    List<Supplier> findSupplierByName(String value);

    public String deleteSupplier(String _id);
    public Supplier getSupplier(String _id);
    public List<Supplier> getAllSupplier();
}
