package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.User;
import com.WarehouseAPI.WarehouseAPI.repository.UserRepository;
import com.WarehouseAPI.WarehouseAPI.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> addUser(User user) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
        if (existingUser.isPresent()) {
            return new ResponseEntity<>("Username is already taken.", HttpStatus.CONFLICT);
        }
        userRepository.save(user);
        return new ResponseEntity<>("User added successfully.", HttpStatus.CREATED);
    }


    @Override
    public String updateUser(String _id, User user) {
        User existingUser = userRepository.findById(_id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + _id));
        existingUser.setUsername(user.getUsername());
        existingUser.setPasswordHash(user.getPasswordHash());
        existingUser.setInformation(user.getInformation());
        userRepository.save(existingUser);
        return "User updated successfully.";
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

    public User getUserByUsername(String username) {
        if (userRepository.findByUsername(username).getUsername().isEmpty())
            return null;
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
