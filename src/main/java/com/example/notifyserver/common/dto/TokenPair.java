package com.example.notifyserver.common.dto;

public record TokenPair(
        String accessToken, String refreshToken
) {
}
