package com.example.notifyserver.crawler.service;

import com.example.notifyserver.common.constants.NoticeConstants;
import com.example.notifyserver.common.repository.NoticeRepository;
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

import java.time.Duration;

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
}