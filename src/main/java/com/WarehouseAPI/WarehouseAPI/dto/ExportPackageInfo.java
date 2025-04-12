package com.WarehouseAPI.WarehouseAPI.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExportPackageInfo {
    private String id;
    private String packageName;
    private Date exportDate;
    private BigDecimal totalSellingPrice;
}
