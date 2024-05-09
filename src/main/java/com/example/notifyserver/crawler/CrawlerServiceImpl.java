package com.example.notifyserver.crawler;

import com.example.notifyserver.common.domain.NoticeType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Base64;
import java.util.Map;
@Service
public class CrawlerServiceImpl implements CrawlerService{

    @org.springframework.beans.factory.annotation.Value("${skku.username}")
    private String username;

    @Value("${skku.password}")
    private String password;

    /**
     * 공통 공지사항에 접근하는 경우 학교 사이트에 로그인 후 게시판 버튼을 클릭하여 게시판 페이지에 진입한다.
     */
    @Override
    public void loginAndGoToComNoticePage() {
        username = new String(Base64.getDecoder().decode(username));
        password = new String(Base64.getDecoder().decode(password));


        // Headless 모드로 Chrome 실행
        ChromeOptions options = new ChromeOptions();
        // Headless 모드 활성화
        options.addArguments("--headless");
        // WebDriver 인스턴스 생성
        WebDriver driver = new ChromeDriver(options);

        // 웹 페이지 열기
        driver.get("https://eportal.skku.edu");

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
        WebElement boardButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#mypage > form:nth-child(16) > div > div.pageHeader > div.mainMenu > div > ul > li.board > a > span.ico > img")));
        boardButton.click();

        // WebDriver 종료
        driver.quit();
    }

    @Override
    public Map<String, String> getLastTwoNotices(NoticeType noticeType) {
        return null;
    }

    @Override
    public int getNewNoticeCount(NoticeType noticeType) {
        return 0;
    }
}
