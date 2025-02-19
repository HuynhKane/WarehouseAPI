package com.WarehouseAPI.WarehouseAPI.dto;

import com.WarehouseAPI.WarehouseAPI.model.Genre;
import com.WarehouseAPI.WarehouseAPI.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class GenreByDateResponse {
    private int day;
    private int month;
    private int year;
    private Genre genre;

}
