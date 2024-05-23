package com.example.notifyserver.user.dto.response;

import com.example.notifyserver.keyword.domain.Keyword;

import java.util.List;

public record UserProfileResponse(
        String nickname,
        String email,
        String major,
        List<String> keywords
) {
}
