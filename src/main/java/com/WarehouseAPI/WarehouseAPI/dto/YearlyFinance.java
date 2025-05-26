package com.WarehouseAPI.WarehouseAPI.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class YearlyFinance {
    private int year;
    private List<MonthlyFinance> months;

    // getters & setters
}
