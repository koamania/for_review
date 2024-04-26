package com.user.createUser.controller;

import com.user.createUser.controller.dto.RegisterUserRequestDto;
import com.user.createUser.controller.dto.UpdateUserRequestDto;
import com.user.createUser.controller.dto.UserResponseDto;
import com.user.createUser.domain.entity.UserEntity;
import com.user.createUser.service.UserService;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<String> joinUser(@RequestBody RegisterUserRequestDto user) {
        try {
            userService.joinUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/user/list")
    public ResponseEntity<Page<UserResponseDto>> getUsers(@RequestParam(required = false, defaultValue = "1") int page,
                                                          @RequestParam(required = false, defaultValue = "5") int pageSize,
                                                          @RequestParam(required = false, defaultValue = "id") String sort) {
        Page<UserResponseDto> paging = userService.findUsers(page, pageSize, sort);

        return ResponseEntity.ok().body(paging);
    }

    @PutMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        try {
            UserEntity update = userService.update(id, updateUserRequestDto);
            return ResponseEntity.ok().body(update);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
