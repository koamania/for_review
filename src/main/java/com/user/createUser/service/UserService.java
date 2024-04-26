package com.user.createUser.service;

import com.user.createUser.controller.dto.RegisterUserRequestDto;
import com.user.createUser.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void joinUser(RegisterUserRequestDto user) {
        log.info("user info - email : {}", user.getEmail());
        userRepository.save(user.toEntity());
        log.info("join service layer 동작 완료");
    }
}
