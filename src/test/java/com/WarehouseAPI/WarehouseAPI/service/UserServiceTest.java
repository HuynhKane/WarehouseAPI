package com.WarehouseAPI.WarehouseAPI.service;

import com.WarehouseAPI.WarehouseAPI.model.User;
import com.WarehouseAPI.WarehouseAPI.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testCreateUser_success() {
        // Arrange
        User user = new User();
        user.setUsername("john");
        user.setPasswordHash("123456");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User saved = userService.getUser(user.get_id()); // method bạn tự viết

        // Assert
        assertEquals("john", saved.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
