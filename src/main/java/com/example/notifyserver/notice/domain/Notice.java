package com.example.notifyserver.notice.domain;

import com.example.notifyserver.common.domain.BaseTimeEntity;
import com.example.notifyserver.common.domain.NoticeCategory;
import com.example.notifyserver.common.domain.NoticeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Notice extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @NotNull
    private String noticeTitle;

    private String noticeUrl;

    private Date noticeDate;

    @Enumerated(EnumType.STRING)
    private NoticeCategory noticeCategory;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

}
