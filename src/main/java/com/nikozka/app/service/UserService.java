package com.nikozka.app.service;

import com.nikozka.app.dto.UserDTO;
import com.nikozka.app.entity.UserEntity;
import com.nikozka.app.exceptions.UserAlreadyExistException;
import com.nikozka.app.repository.UserRepository;
import com.nikozka.app.utils.UserTableCreation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserTableCreation userEntityTableCreation;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDTO userDTO) {
        userEntityTableCreation.createTableIfNotExists();
        if(userRepository.findByUsername(userDTO.getUsername()) != null){
            throw new UserAlreadyExistException("A user with the specified username already exists");
        }
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        String encryptedPassword = passwordEncoder.encode(password);
        UserEntity userEntity = new UserEntity(username, encryptedPassword);

        userRepository.saveAndFlush(userEntity);
    }
}
