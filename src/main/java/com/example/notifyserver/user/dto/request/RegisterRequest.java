package com.example.notifyserver.user.dto.request;

public record RegisterRequest(
        String nickname,
        String email,
        String userMajor
) {
}
