package com.WarehouseAPI.WarehouseAPI.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductByStorageResponse {
    private String storageLocationId;
    private List<ProductResponse> listProduct;
}
