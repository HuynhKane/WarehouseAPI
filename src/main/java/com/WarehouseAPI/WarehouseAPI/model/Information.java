package com.WarehouseAPI.WarehouseAPI.model;

public class Information {
    private String idInformation;

    private String firstName;

    private String lastName;

    private String email;

    private String role;

    private String picture;

    public String getIdInformation() {
        return idInformation;
    }

    public void setIdInformation(String idInformation) {
        this.idInformation = idInformation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
