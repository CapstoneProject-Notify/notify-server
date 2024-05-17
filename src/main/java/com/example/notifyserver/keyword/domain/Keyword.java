package com.example.notifyserver.keyword.domain;

import com.example.notifyserver.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @NotNull
    private User user;

    @NotNull
    private String userKeyword;
}
