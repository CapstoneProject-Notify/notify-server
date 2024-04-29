package com.example.notifyserver.user.dto.request;

import com.example.notifyserver.user.domain.UserMajor;

public record RegisterRequest(
        String nickname,
        String email,
        UserMajor userMajor
) {
}
