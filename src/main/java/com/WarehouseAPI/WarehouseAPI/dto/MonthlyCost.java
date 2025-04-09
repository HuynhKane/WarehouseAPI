package com.WarehouseAPI.WarehouseAPI.dto;

import java.math.BigDecimal;

public class MonthlyCost {
    private int month;
    private BigDecimal totalCost;

    // Getters and Setters

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
