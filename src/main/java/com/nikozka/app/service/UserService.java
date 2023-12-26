package com.nikozka.app.service;

import com.nikozka.app.entity.UserEntity;
import com.nikozka.app.repository.UserRepository;
import com.nikozka.app.utils.UserEntityTableCreation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserEntityTableCreation userEntityTableCreation;

    @Override
    public void saveUser(UserEntity userEntity) {
        userEntityTableCreation.createTableIfNotExists();
        userRepository.saveAndFlush(userEntity);
    }
}

