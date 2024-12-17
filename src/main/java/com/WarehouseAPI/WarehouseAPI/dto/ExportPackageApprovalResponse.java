package com.WarehouseAPI.WarehouseAPI.dto;

import com.WarehouseAPI.WarehouseAPI.model.ExportPackage;

import java.math.BigDecimal;

public class ExportPackageApprovalResponse {
    private ExportPackage exportPackage;
    private BigDecimal totalSellingPrice;

    // Constructor, Getters, and Setters
    public ExportPackageApprovalResponse(ExportPackage exportPackage, BigDecimal totalSellingPrice) {
        this.exportPackage = exportPackage;
        this.totalSellingPrice = totalSellingPrice;
    }

    public ExportPackage getExportPackage() {
        return exportPackage;
    }

    public void setExportPackage(ExportPackage exportPackage) {
        this.exportPackage = exportPackage;
    }

    public BigDecimal getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }
}
