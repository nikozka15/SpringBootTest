package com.nikozka.app.controllers;

import com.nikozka.app.config.JwtUtils;
import com.nikozka.app.dto.AuthenticationRequest;
import com.nikozka.app.dto.UserDTO;
import com.nikozka.app.entity.UserEntity;
import com.nikozka.app.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final BCryptPasswordEncoder passwordEncoder;
    private final IUserService userService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/add")
    public ResponseEntity<Void> add(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        String encryptedPassword = passwordEncoder.encode(password);
        UserEntity newUser = new UserEntity(username, encryptedPassword);

        userService.saveUser(newUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        if (userDetails != null) {
            return ResponseEntity.ok(jwtUtils.generateToken(userDetails));
        }
        return ResponseEntity.status(400).body("Some error has occurred"); // todo will I need this line
    }
}
