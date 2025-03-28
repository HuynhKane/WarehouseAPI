package com.WarehouseAPI.WarehouseAPI.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyRevenue {
    private int month;
    private BigDecimal totalRevenue;
}
