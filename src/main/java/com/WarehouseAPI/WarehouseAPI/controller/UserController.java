package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.dto.AddUserRequest;
import com.WarehouseAPI.WarehouseAPI.model.Information;
import com.WarehouseAPI.WarehouseAPI.model.User;
import com.WarehouseAPI.WarehouseAPI.repository.UserRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final IUserService userService;
    @Autowired
    private UserRepository userRepository;

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
    public ResponseEntity<?> addUser(@RequestBody AddUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        Information info = new Information();
        info.setFirstName(request.getInformation().getFirstName());
        info.setLastName(request.getInformation().getLastName());
        info.setEmail(request.getInformation().getEmail());
        info.setPicture(request.getInformation().getPicture());

        // Set default or validated role (e.g., only admin can assign)
        String requestedRole = request.getInformation().getRole();
        info.setRole(requestedRole != null ? requestedRole : "USER");

        // Link information to user
        user.setInformation(info);
        // Save
        userRepository.save(user);

        return ResponseEntity.ok("User created successfully");
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