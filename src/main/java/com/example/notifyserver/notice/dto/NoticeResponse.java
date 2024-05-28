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
    private boolean isScrapped;
    private String url;
}
