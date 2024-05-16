package com.example.notifyserver.esm_notice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EsmNotice {
    @Id
    private Long noticeId;

}
