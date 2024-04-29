package com.example.notifyserver.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    private String googleId;


    private String nickName;


    private String email;

    @Enumerated(value = EnumType.STRING)
    private UserMajor userMajor;

    public void update(String nickName, String email, UserMajor userMajor){
        this.nickName = nickName;
        this.email = email;
        this.userMajor = userMajor;
    }

}
