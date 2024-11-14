package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.User;
import com.WarehouseAPI.WarehouseAPI.service.IUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUserDetails() {
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public User getUserDetails(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PostMapping
    public String addUserDetails(@RequestBody User user) {
        userService.addUser(user);
        return "User added successfully";
    }

    @PutMapping("/{id}")
    public String updateUserDetails(@PathVariable String id, @RequestBody User updated) {
        userService.updateUser(id, updated);
        return "User updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteUserDetails(@PathVariable String id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }
}