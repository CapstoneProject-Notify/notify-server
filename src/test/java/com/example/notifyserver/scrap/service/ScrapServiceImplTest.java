package com.example.notifyserver.scrap.service;

import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.scrap.domain.Scrap;
import com.example.notifyserver.scrap.repository.ScrapRepository;
import com.example.notifyserver.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class ScrapServiceImplTest {

    @Autowired
    ScrapService scrapService;
    @Autowired
    ScrapRepository scrapRepository;
    @Autowired
    EntityManager em;

    @Test
    void doScrap() {

        //given
        User user = User.builder()
                .googleId("구글아이디")
                .nickName("닉네임")
                .email("이메일")
                .userMajor("bus")
                .build();

        Notice notice = Notice.builder()
                .noticeTitle("공지 제목")
                .noticeType(NoticeType.COM)
                .noticeUrl("공지 url")
                .build();

        //when
        em.persist(user);
        em.persist(notice);
        long findId = scrapService.doScrap(user, notice);
        Scrap scrappedNotice = scrapRepository.findById(findId).orElse(null);

        //then
        assert scrappedNotice != null;
        Assertions.assertThat(scrappedNotice.getScrapId()).isEqualTo(findId);
    }
}