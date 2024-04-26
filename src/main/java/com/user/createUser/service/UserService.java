package com.user.createUser.service;

import com.user.createUser.controller.dto.RegisterUserRequestDto;
import com.user.createUser.controller.dto.UserResponseDto;
import com.user.createUser.domain.entity.UserEntity;
import com.user.createUser.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public Page<UserResponseDto> findUsers(int page, int pageSize, String sort) {
        int sendPage = page - 1;

        // 비밀번호 제거 (Entity > DTO 변경)
        Page<UserEntity> lists = userRepository.findAll(PageRequest.of(sendPage, pageSize, Sort.by(Sort.Direction.ASC, sort)));
        Page<UserResponseDto> userResponseDto = lists.map(user -> new UserResponseDto(user.getId(), user.getNickname(), user.getName(), user.getPhone(), user.getEmail()));

        return userResponseDto;
    }
}
