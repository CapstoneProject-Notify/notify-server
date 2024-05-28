package com.example.notifyserver.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class NoticeResponse {
    private long noticeId;
    private String title;
    private Date noticeDate;
    private boolean isScrapped;
    private String url;
}
