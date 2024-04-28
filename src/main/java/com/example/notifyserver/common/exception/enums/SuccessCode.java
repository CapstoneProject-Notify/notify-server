package com.example.notifyserver.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {


    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공입니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공입니다");

    private final HttpStatus httpStatus;
    private final String message;
}
