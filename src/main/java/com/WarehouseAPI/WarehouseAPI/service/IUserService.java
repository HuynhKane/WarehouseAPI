package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.Report;
import com.WarehouseAPI.WarehouseAPI.model.User;

import java.util.List;

public interface IUserService {
    public String addUser(User user);
    public String updateUser(String _id, User user);
    public String deleteUser(String _id);
    public User getUser(String _id);
    public List<User> getAllUser();
}
