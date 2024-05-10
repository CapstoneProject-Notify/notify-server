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

    /**
     * DB에서 가장 최신의 글 2개의 제목과 날짜를 가져온다.
     * @param noticeType 공지사항의 타입
     * @return 제목과 날짜를 매핑한 객체
     */
    @Override
    public String [][] getLastTwoNotices(NoticeType noticeType) {
        List<Notice> top2 = repository.findTop2ByOrderByCreatedAtDesc(noticeType);
        String [][] result = new String[2][2]; // 각 행의 0번째 인덱스에는 제목이 1번쨰 인덱스에는 날짜가 들어있음. 0번쨰 행이 더 최신 글임

        for (int i=0; i<2; i++) {
            String noticeTitle = top2.get(i).getNoticeTitle();
            Date noticeDate = top2.get(i).getNoticeDate();
            result[i][0] = noticeTitle;
            result[i][1] = noticeDate.toString();
        }
        return result;
    }

    @Override
    public int getNewNoticeCount(NoticeType noticeType, WebDriver driver, String[][] top2) {
        return 0;
    }

    /**
     * 공지사항 페이지에서 공지사항의 제목과 날짜를 가져온다.
     * @param driver 크롬 드라이버
     * @return 공지사항 제목과 날짜 리스트
     */
    @NotNull
    @Override
    public TitlesAndDates getTitlesAndDates(WebDriver driver) {

        driver.get(NoticeConstants.BOARD_PAGE);

        // 모든 iframe 요소 가져오기
        WebElement secondIframe = driver.findElement(By.xpath("(//iframe)[2]"));
        // 두 번째 iframe으로 전환
        driver.switchTo().frame(secondIframe);

        // 클래스 이름이 .ev_dhx_terrace 인 모든 요소 가져오기
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        List<WebElement> dynamicElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".ev_dhx_terrace")));

        // 타이틀과 날짜를 저장할 리스트 생성
        List<String> titles = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        // 가져온 요소들에서 타이틀과 날짜 추출하여 리스트에 저장
        for (WebElement element : dynamicElements) {
            // 타이틀 추출
            WebElement titleElement = element.findElement(By.cssSelector("span"));
            titles.add(titleElement.getText());

            // 날짜 추출
            WebElement dateElement = element.findElement(By.cssSelector("td:nth-child(6)"));
            dates.add(dateFormating(dateElement.getText()));
        }

        // TitlesAndDates 객체에 저장해 반환
        return new TitlesAndDates(titles, dates);
    }

    /**
     * 날짜 형식을 바꾸는 메서드
     * @param dateString 페이지에서 가져온 날짜 텍스트
     * @return DB에 저장할 날짜 형식을 문자열로 바꾼 값
     */
    @Override
    public String dateFormating(String dateString) {
        // SimpleDateFormat을 사용하여 기존 형식의 문자열을 Date 객체로 파싱
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd (E) HH:mm");
        Date date;
        try {
            date = inputFormat.parse(dateString);
            // 원하는 형식의 문자열로 변환
            SimpleDateFormat outputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            // 결과 반환
            return outputFormat.format(date);

        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
    }
}
