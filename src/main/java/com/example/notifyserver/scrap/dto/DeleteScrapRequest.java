package com.example.notifyserver.scrap.dto;

import com.example.notifyserver.common.domain.NoticeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

@NotNull
public record DeleteScrapRequest(
    @NotNull
    @Enumerated(EnumType.STRING)
    NoticeType type,
    @NotNull
    long userId,
    @NotNull
    long noticeId) {
}
