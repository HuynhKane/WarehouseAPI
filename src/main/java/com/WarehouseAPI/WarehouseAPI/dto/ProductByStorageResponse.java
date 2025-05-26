package com.WarehouseAPI.WarehouseAPI.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductByStorageResponse {
    private String storageLocationId;
    private String storageName;
    private String storageImage;
    private List<ProductResponse> listProduct;
}
