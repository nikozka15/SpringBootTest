package com.nikozka.app.service;

import com.nikozka.app.entity.UserEntity;
import com.nikozka.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(userName);
        if (userEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with username: " + userName);
        }
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
