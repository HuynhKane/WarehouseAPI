package com.WarehouseAPI.WarehouseAPI.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class YearlyFinance {
    private int year;
    private Map<Integer, MonthlyFinance> months;

    // getters & setters
}
