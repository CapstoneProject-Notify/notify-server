package com.example.notifyserver.crawler.service;

import com.example.notifyserver.common.constants.NoticeConstants;
import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.common.repository.NoticeRepository;
import com.example.notifyserver.crawler.dto.TitlesAndDates;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CrawlerServiceImplTest {
    @Autowired
    CrawlerService service;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    EntityManager em;

    // Headless 모드로 Chrome 실행
    ChromeOptions options = new ChromeOptions().addArguments("--headless");
    WebDriver webDriver = new ChromeDriver(options);

    @Test
    void loginAndGoToComNoticePage() {
        //given
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        service.loginAndGoToComNoticePage(webDriver);

        //when
        // 로그인을 해야지만 들어갈 수 있는 링크
        webDriver.get(NoticeConstants.BOARD_PAGE);
        // 로그인 form이 있는지 확인

        //then
        // 정상적으로 로그인 했다면 로그인 폼이 없어야 함
        Assertions.assertThrows(TimeoutException.class, () -> wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input#userID"))));
        webDriver.close();
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
        assertEquals(notice1.getNoticeTitle(), lastTwoNotices[1][0]);
        assertEquals(notice1.getNoticeDate().toString(), lastTwoNotices[1][1]);
        assertEquals(notice2.getNoticeTitle(), lastTwoNotices[0][0]);
        assertEquals(notice2.getNoticeDate().toString(), lastTwoNotices[0][1]);
    }

}