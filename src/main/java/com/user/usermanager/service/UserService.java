package com.user.usermanager.service;

import com.user.usermanager.controller.dto.RegisterUserRequestDto;
import com.user.usermanager.controller.dto.UpdateUserRequestDto;
import com.user.usermanager.controller.dto.UserResponseDto;
import com.user.usermanager.domain.entity.UserEntity;
import com.user.usermanager.domain.repository.UserRepository;
import com.user.usermanager.utils.Encryption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void joinUser(RegisterUserRequestDto user) {
        UserEntity userData = user.toEntity();

        // email 중복 검사
        UserEntity isExist = userRepository.findByEmail(userData.getEmail());
        if(isExist != null) {
            throw new IllegalArgumentException("이미 가입된 이메일 주소입니다.");
        }

        // nickname 길이 제한
        if(userData.getNickname().length() > 15) {
            throw new IllegalArgumentException("nickname 길이는 15자를 넘을 수 없습니다.");
        }

        // phone 번호 정규식 검증
        String phoneRegularExpression = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
        if(!Pattern.matches(phoneRegularExpression, userData.getPhone())) {
            throw new IllegalArgumentException("유효하지 않은 전화번호 형식입니다.");
        }

        // email 정규식 검증
        String emailRegularExpression = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(emailRegularExpression, user.getEmail())) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }

        // password 암호화
        Encryption hashPwd = new Encryption();
        String newPassword = hashPwd.getEncrypt(userData.getPassword(), hashPwd.salt);

        userData.encryptedPwd(newPassword);
        log.info("변경 비밀번호 : {}", newPassword);

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

    @Transactional
    public UserEntity update(long id, UpdateUserRequestDto dto) {

        // 아이디 존재 유무 확인
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("아이디를 찾을 수 없습니다. : " + id));

        // nickname 길이 제한
        if(dto.getNickname().length() > 15) {
            throw new IllegalArgumentException("nickname 길이는 15자를 넘을 수 없습니다.");
        }

        // phone 번호 정규식 검증
        String phoneRegularExpression = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
        if(!Pattern.matches(phoneRegularExpression, dto.getPhone())) {
            throw new IllegalArgumentException("유효하지 않은 전화번호 형식입니다.");
        }

        // email 정규식 검증
        String emailRegularExpression = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(emailRegularExpression, dto.getEmail())) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }

        // password 암호화
        Encryption hashPwd = new Encryption();
        String newPassword = hashPwd.getEncrypt(dto.getPassword(), hashPwd.salt);
        log.info("변경 비밀번호 : {}", newPassword);

        user.update(newPassword, dto.getNickname(), dto.getName(), dto.getPhone());

        return user;
    }
}
