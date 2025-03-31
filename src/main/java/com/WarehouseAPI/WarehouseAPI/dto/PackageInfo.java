package com.WarehouseAPI.WarehouseAPI.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PackageInfo {
    private String id;
    private String packageName;
    private BigDecimal totalSellingPrice;
}
