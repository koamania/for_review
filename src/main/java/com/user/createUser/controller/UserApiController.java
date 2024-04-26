package com.user.createUser.controller;

import com.user.createUser.controller.dto.RegisterUserRequestDto;
import com.user.createUser.domain.entity.UserEntity;
import com.user.createUser.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserApiController {
    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/user/join")
    public ResponseEntity<UserEntity> joinUser(@RequestBody RegisterUserRequestDto user) {

        userService.joinUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
