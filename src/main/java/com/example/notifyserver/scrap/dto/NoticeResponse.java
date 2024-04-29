package com.example.notifyserver.scrap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class NoticeResponse {
    long scrapId;
    String title;
    String url;
    boolean isScrapped;
    
    // Json 응답 시 is가 사라지는 것을 방지
    @JsonProperty("isScrapped")
    public boolean isScrapped() {
        return isScrapped;
    }
}

