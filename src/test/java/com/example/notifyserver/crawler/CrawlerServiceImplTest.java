package com.example.notifyserver.crawler;

import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.common.repository.NoticeRepository;
import com.example.notifyserver.crawler.service.CrawlerService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
@SpringBootTest
@Transactional
class CrawlerServiceImplTest {
    @Autowired
    CrawlerService service;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    EntityManager em;

    @Test
    void loginAndGoToComNoticePage() {
        service.loginAndGoToComNoticePage();
    }

    @Test
    public void getLastTwoNotices() throws Exception{

        //given
        Notice notice1 = Notice.builder().
                noticeTitle("게시물").
                noticeDate(new Date(124, 5, 1)).
                noticeType(NoticeType.COM).
                noticeUrl("url2").
                build();
        Notice notice2 = Notice.builder().
                noticeTitle("가장 최근 게시물").
                noticeDate(new Date(124, 5, 2)).
                noticeType(NoticeType.COM).
                noticeUrl("url1").
                build();

        //when
        em.persist(notice1);
        em.persist(notice2);
        String[][] lastTwoNotices = service.getLastTwoNotices(NoticeType.COM);

        //then
        Assertions.assertEquals(notice1.getNoticeTitle(), lastTwoNotices[1][0]);
        Assertions.assertEquals(notice1.getNoticeDate().toString(), lastTwoNotices[1][1]);
        Assertions.assertEquals(notice2.getNoticeTitle(), lastTwoNotices[0][0]);
        Assertions.assertEquals(notice2.getNoticeDate().toString(), lastTwoNotices[0][1]);
    }
}