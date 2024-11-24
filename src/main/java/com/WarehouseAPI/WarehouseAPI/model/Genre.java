package com.WarehouseAPI.WarehouseAPI.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "genre")
public class Genre {
    @Id
    private String _id;
    private String genreName;

    public String get_id() {
        return _id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
