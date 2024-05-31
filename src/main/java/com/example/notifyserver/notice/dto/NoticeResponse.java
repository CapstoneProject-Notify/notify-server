package com.example.notifyserver.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class NoticeResponse {
    private long noticeId;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date noticeDate;
    // Json 응답 시 is가 사라지는 것을 방지
    @JsonProperty("isScrapped")
    private boolean isScrapped;
    private String url;
}
