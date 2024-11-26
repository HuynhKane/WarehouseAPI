package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.User;
import com.WarehouseAPI.WarehouseAPI.repository.UserRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public String addUser(User user) {
        userRepository.save(user);
        return "Add user, done";
    }

    @Override
    public String updateUser(String _id, User user) {
        Optional<User> existingUser = userRepository.findById(_id);
        if(existingUser.isPresent()){
            User existUser = existingUser.get();
            existUser.setUsername(user.getUsername());
            existUser.setPasswordHash(user.getPasswordHash());
            existUser.setInformation(user.getInformation());
            userRepository.save(existUser);
        }
        return "Update user, done";
    }

    @Override
    public String deleteUser(String _id) {
        userRepository.deleteById(_id);
        return "Delete user, done";
    }

    @Override
    public User getUser(String _id) {
        if (userRepository.findById(_id).isEmpty())
            return null;
        return userRepository.findById(_id).get();
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
