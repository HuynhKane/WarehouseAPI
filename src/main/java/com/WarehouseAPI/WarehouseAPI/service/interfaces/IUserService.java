package com.WarehouseAPI.WarehouseAPI.service.interfaces;

import com.WarehouseAPI.WarehouseAPI.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    public ResponseEntity<String> addUser(User user);
    public String updateUser(String _id, User user);
    public String deleteUser(String _id);
    public User getUser(String _id);
    public List<User> getAllUser();
}
