package com.user.createUser.controller;

import com.user.createUser.domain.entity.UserEntity;
import com.user.createUser.domain.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserApiControllerTest {

    @Autowired
    UserRepository userRepository;

    //    join user test
    @Test
    @DisplayName("joinUser : 회원 가입 성공")
    public void joinUser() throws Exception {
        // given
        UserEntity entity = new UserEntity(1L, "password", "nickname", "name", "010-1234-5678", "1@naver.com", LocalDateTime.now(), LocalDateTime.now());

        // when
        userRepository.save(entity);

        // then
        Optional<UserEntity> result = userRepository.findById(1L);

        assertThat(result.isPresent());
        assertThat(result.get().getEmail()).isEqualTo(entity.getEmail());
    }

    @Test
    @DisplayName("selectAll : 회원 정보 조회 성공")
    public void selectAll() throws Exception {
        // given
        UserEntity entity1 = new UserEntity(1L, "password", "nickname", "name", "010-1234-5678", "1@naver.com", LocalDateTime.now(), LocalDateTime.now());
        UserEntity entity2 = new UserEntity(2L, "password", "nickname", "name", "010-1234-5678", "2@naver.com", LocalDateTime.now(), LocalDateTime.now());

        // when
        userRepository.save(entity1);
        userRepository.save(entity2);
        List<UserEntity> result = userRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(2);

    }
}