package com.example.notifyserver.scrap.domain;

import com.example.notifyserver.notice.domain.Notice;
import com.example.notifyserver.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "noticeId"})
})
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticeId")
    @NotNull
    private Notice notice;

    public Scrap(User user, Notice notice){
        this.user = user;
        this.notice = notice;
    }

}
