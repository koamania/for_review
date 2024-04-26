package com.user.createUser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.createUser.controller.dto.RegisterUserRequestDto;
import com.user.createUser.domain.entity.UserEntity;
import com.user.createUser.domain.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class UserApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
    }

    //    join user test
    @Test
    @DisplayName("joinUser : 회원 가입 성공")
    public void joinUser() throws Exception {

        // given
        final String url = "/api/user/join";
        final String password = "010203abc";
        final String nickname = "j";
        final String name = "jungjunho";
        final String phone = "010-1234-5678";
        final String email = "juno@naver.com";
        final RegisterUserRequestDto registerUserRequestDto = new RegisterUserRequestDto(password, nickname, name, phone, email);
        final String requestBody = objectMapper.writeValueAsString(registerUserRequestDto);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        Optional<UserEntity> user = userRepository.findById(1L);

        // 존재 여부 확인
        assertThat(user).isPresent();
        // value 동일 여부 확인
        assertThat(user.get().getPassword()).isEqualTo(password);
        assertThat(user.get().getNickname()).isEqualTo(nickname);
        assertThat(user.get().getName()).isEqualTo(name);
        assertThat(user.get().getPhone()).isEqualTo(phone);
        assertThat(user.get().getEmail()).isEqualTo(email);
    }
}