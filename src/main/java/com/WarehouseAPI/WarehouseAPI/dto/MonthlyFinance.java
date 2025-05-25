package com.WarehouseAPI.WarehouseAPI.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class MonthlyFinance {
    private BigDecimal cost;
    private BigDecimal revenue;
    private boolean profit;


}
