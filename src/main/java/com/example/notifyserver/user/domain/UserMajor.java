package com.example.notifyserver.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserMajor {
    COM("com"),    // 공통 공지사항
    BUS("bus"),    // 경영학과 공지사항
    COS("cos"),    // 유학동양학과 공지사항
    AAI("aai"),    // 인공지능융합학과 공지사항
    ESM("esm");     // 시스템경영공학과 공지사항

    private final String value;
}
