package com.nikozka.app.service;

import com.nikozka.app.dto.UserDTO;
import com.nikozka.app.entity.UserEntity;
import com.nikozka.app.exceptions.UserNotSavedException;
import com.nikozka.app.repository.UserRepository;
import com.nikozka.app.utils.UserTableCreation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTableCreation userEntityTableCreation;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUserTestSuccessful() {
        UserDTO userDTO = new UserDTO("testUser", "testPassword");
        doNothing().when(userEntityTableCreation).createTableIfNotExists();
        when(passwordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(new UserEntity());

        userService.saveUser(userDTO);

        verify(userEntityTableCreation, times(1)).createTableIfNotExists();
        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(userRepository, times(1)).saveAndFlush(any(UserEntity.class));
    }
    @Test
    void saveUserTestUserNotSavedException() {
        UserDTO userDTO = new UserDTO("testUser", "testPassword");
        doNothing().when(userEntityTableCreation).createTableIfNotExists();
        when(passwordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(null);

        assertThrows(UserNotSavedException.class, () -> userService.saveUser(userDTO));
    }
}