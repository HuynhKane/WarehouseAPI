package com.WarehouseAPI.WarehouseAPI.controller;

import com.WarehouseAPI.WarehouseAPI.model.User;
import com.WarehouseAPI.WarehouseAPI.security.JwtUtils;
import com.WarehouseAPI.WarehouseAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public Map<String, String> login(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.getUserByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            String token = jwtUtils.generateToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("id", user.get_id().toString());
            response.put("role", user.getInformation().getRole().toString());

            return response;
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @GetMapping("/logout")
    public String logout() {
        // Client chỉ cần xóa token trên frontend
        return "Logged out successfully!";
    }
}

