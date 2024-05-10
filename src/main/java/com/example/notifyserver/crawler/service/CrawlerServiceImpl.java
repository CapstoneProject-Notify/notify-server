package com.example.notifyserver.crawler.service;

import com.example.notifyserver.common.constants.NoticeConstants;
import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.common.exception.enums.ErrorCode;
import com.example.notifyserver.common.exception.model.NotFoundException;
import com.example.notifyserver.crawler.dto.TitlesAndDates;
import com.example.notifyserver.crawler.repository.CrawlerRepository;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

@Service
public class CrawlerServiceImpl implements CrawlerService{

    @Autowired
    CrawlerRepository repository;

    @org.springframework.beans.factory.annotation.Value("${skku.username}")
    private String username;

    @Value("${skku.password}")
    private String password;

    /**
     * 공통 공지사항에 접근하는 경우 학교 사이트에 로그인 후 게시판 버튼을 클릭하여 게시판 페이지에 진입한다.
      * @param driver 크롬 드라이버
     */
    @Override
    public void loginAndGoToComNoticePage(WebDriver driver) {
        username = new String(Base64.getDecoder().decode(username));
        password = new String(Base64.getDecoder().decode(password));

        // 웹 페이지 열기
        driver.get(NoticeConstants.LOGIN_PAGE);

        // 아이디와 비밀번호 입력
        WebElement usernameInput = driver.findElement(By.id("userid"));
        WebElement passwordInput = driver.findElement(By.id("userpwd"));
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);

        // 로그인 버튼 클릭
        WebElement loginButton = driver.findElement(By.id("loginBtn"));
        loginButton.click();

        // 게시판 버튼 클릭
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement boardButton = wait.until(ExpectedConditions.presenceOfElementLocated
                (By.cssSelector("#mypage > form:nth-child(16) > div > div.pageHeader > div.mainMenu > div > ul > li.board > a > span.ico > img")));
        boardButton.click();
    }

    @Override
    public String[][] getLastTwoNotices(NoticeType noticeType) {
        return new String[0][];
    }

    @Override
    public int getNewNoticeCount(NoticeType noticeType, WebDriver driver, String[][] top2) {
        return 0;
    }
}
