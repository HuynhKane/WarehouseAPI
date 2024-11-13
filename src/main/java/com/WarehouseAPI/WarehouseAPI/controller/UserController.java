package com.WarehouseAPI.WarehouseAPI.controller;


import com.WarehouseAPI.WarehouseAPI.model.Product;
import com.WarehouseAPI.WarehouseAPI.model.User;
import com.WarehouseAPI.WarehouseAPI.service.IUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    IUserService iUserService;

    public UserController(IUserService iUserService){
        this.iUserService = iUserService;

    }

    @GetMapping("/all")
    public List<User> getAllUserDetails(){
        return iUserService.getAllUser();
    }

    @GetMapping("/{_id}/get")
    public User getUserDetails(@PathVariable("_id") String _id){
        return iUserService.getUser(_id);
    }

    @PostMapping("/add")
    public String addUserDetails(@RequestBody User user){
        iUserService.addUser(user);
        return "Product was created successfully";
    }

    @PutMapping("/{_id}/update")
    public String updateUserDetails(@PathVariable("_id") String _id, @RequestBody User updated){
        iUserService.updateUser(_id, updated);
        return "Product was updated successfully";
    }

    @DeleteMapping("/{_id}/delete")
    public String deleteUserDetails(@PathVariable("_id") String _id){
        iUserService.deleteUser(_id);
        return "This was deleted successfully";
    }
}
