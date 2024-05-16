package com.example.notifyserver.bus_notice.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BusNotice {
    @Id
    private Long noticeId;

}
