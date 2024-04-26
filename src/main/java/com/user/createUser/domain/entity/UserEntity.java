package com.user.createUser.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickName", nullable = false)
    private String nickname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name= "email", nullable = false, unique = true)
    private String email;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public UserEntity(Long id, String password, String nickname, String name, String phone, String email, LocalDateTime createdDate) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.createdDate = createdDate;
    }
}
