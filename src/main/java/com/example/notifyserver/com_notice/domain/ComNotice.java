package com.example.notifyserver.com_notice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ComNotice{
    @Id
    private Long noticeId;

}
