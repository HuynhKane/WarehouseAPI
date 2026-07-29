package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.dto.LoginRequest;
import com.WarehouseAPI.WarehouseAPI.model.User;
import com.WarehouseAPI.WarehouseAPI.security.JwtUtils;
import com.WarehouseAPI.WarehouseAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userService.addUser(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        User user = userService.getUserByUsername(request.username());
        if (user == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        return Map.of(
                "token", jwtUtils.generateToken(user.getUsername()),
                "id", user.get_id(),
                "role", user.getInformation().getRole()
        );
    }

    @GetMapping("/logout")
    public String logout() {
        // Client chỉ cần xóa token trên frontend
        return "Logged out successfully!";
    }
}
