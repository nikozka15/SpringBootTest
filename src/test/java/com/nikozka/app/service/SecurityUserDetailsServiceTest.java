package com.nikozka.app.service;

import com.nikozka.app.entity.UserEntity;
import com.nikozka.app.exceptions.UserNotFoundException;
import com.nikozka.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityUserDetailsService userDetailsService;

    @Test
    void loadUserByUsernameTestSuccessful() {
        String username = "testUser";
        UserEntity userEntity = new UserEntity(username, "hashedPassword");
        when(userRepository.findByUsername(username)).thenReturn(userEntity);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertAll("UserDetails assertions",
                () -> assertNotNull(userDetails),
                () -> assertEquals(username, userDetails.getUsername()),
                () -> assertEquals("hashedPassword", userDetails.getPassword()),
                () -> assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")))
        );}

    @Test
    void loadUserByUsernameTestUserNotFound() {
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}