package com.WarehouseAPI.WarehouseAPI.model;

import org.springframework.data.annotation.Id;

public class Supplier {

    @Id
    private String _id;
    private String name;
    private  String email;

    private Address address;
    private int ratings;

    public String get_id() {
        return _id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }
}
