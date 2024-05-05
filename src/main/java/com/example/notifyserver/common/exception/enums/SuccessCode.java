package com.example.notifyserver.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {


    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공입니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공입니다"),
    REGISTER_SUCCESS(HttpStatus.OK, "회원가입 성공"),
    DELETE_SUCCESS(HttpStatus.OK, "회원탈퇴 성공"),

    SAVE_SCRAP_SUCCESS(HttpStatus.OK, "스크랩 성공입니다."),
    DELETE_SCRAP_SUCCESS(HttpStatus.OK, "스크랩 제거 성공입니다."),
    GET_SCRAP_SUCCESS(HttpStatus.OK, "스크랩 조회 성공입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
