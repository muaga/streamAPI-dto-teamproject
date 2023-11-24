package com.example.kakao.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Column(length = 100, nullable = false, unique = true)
    private String email; // 인증시 필요한 필드

    @NotEmpty
    @Size(min = 8, max = 20, message = "8자에서 20자 이내여야 합니다.")
    @Column(length = 256, nullable = false)
    private String password;

    @NotEmpty
    @Column(length = 45, nullable = false)
    private String username;

    @Builder
    public User(int id, String email, String password, String username) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
