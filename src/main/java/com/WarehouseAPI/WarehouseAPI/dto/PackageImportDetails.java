package com.WarehouseAPI.WarehouseAPI.dto;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

public class PackageImportDetails {
    private String packageId;
    private String packageName;
    private Date importDate;
    private BigDecimal totalImportPrice;

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public BigDecimal getTotalImportPrice() {
        return totalImportPrice;
    }

    public void setTotalImportPrice(BigDecimal totalImportPrice) {
        this.totalImportPrice = totalImportPrice;
    }
}
