package com.example.notifyserver.scrap.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Builder @Getter
public class GetScrapsResponse {
    long page;
    long totalPages;
    List<NoticeResponse> notices = new ArrayList<>();
}
