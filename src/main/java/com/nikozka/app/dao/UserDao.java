package com.nikozka.app.dao;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDao {
    private static final List<UserDetails> APPLICATION_USERS = Arrays.asList(new User(
                    "vshyrshova@gmail.com", "password", Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))),
            new User(
                    "user.mail@gmail.com", "password", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
    );

    public UserDetails findUserByEmail(String email) {
        return APPLICATION_USERS
                .stream()
                .filter(userDetails -> userDetails.getUsername().equals(email))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));
    }
}
