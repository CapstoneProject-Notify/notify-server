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

    /**
     * 새 글의 개수를 반환한다.
     * @param noticeType 공지사항의 타입
     * @param driver 크롬 드라이버
     * @param top2 DB에 저장된 가장 최근 게시물 2개
     * @return
     */
    @Override
    public int getNewNoticeCount(NoticeType noticeType, WebDriver driver, String [][] top2) {
        // 페이지에서 제목 목록과 날짜 목록을 가져오기
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        TitlesAndDates titlesAndDates = getTitlesAndDates(driver);

        boolean hasNewNotice = newNoticeCheck(top2, titlesAndDates.titles(), titlesAndDates.dates());
        // 새 글이 없는 경우 0을 반환
        if(!hasNewNotice) {return 0;}
        // 새 글이 있는 경우 새 글이 몇개 있는지 반환
        else return findNewNoticeOrder(top2, driver);
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
     * 새 글의 개수를 찾는다.
     * @param top2 DB에 저장된 가장 최근 게시물 2개
     * @param driver 크롬 드라이버
     * @return 새 글의 개수
     */
    @Override
    public int findNewNoticeOrder(String[][] top2, WebDriver driver) {
        String firstTitle = top2[0][0];
        String firstDate = top2[0][1];
        String secondTitle = top2[1][0];
        String secondDate = top2[1][1];
        // 1페이지부터 10페이지까지 반복하면서
        for(int i=1; i<=10; i++){
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.dhx_page:nth-of-type(" + i + ")")));
            nextButton.click();

            TitlesAndDates titlesAndDates = getTitlesAndDates(driver);
            if(titlesAndDates.titles().contains(firstTitle)
                    && titlesAndDates.titles().indexOf(firstTitle) == titlesAndDates.dates().indexOf(firstDate)){
                // DB의 첫번째 게시물의 인덱스를 찾으면 새글의 개수를 반환
                return (i-1) * 15 + titlesAndDates.titles().indexOf(firstTitle);
            }else if(titlesAndDates.titles().contains(secondTitle)
                    && titlesAndDates.titles().indexOf(secondTitle) == titlesAndDates.dates().indexOf(secondDate)){
                // DB의 두번째 게시물의 인덱스를 찾으면 첫번째 게시물이 수정이나 삭제되었다고 여기고 두번째 게시글 이후를 새글로 간주 후 개수를 반환
                return (i-1) * 15 + titlesAndDates.titles().indexOf(secondTitle);
            }
        }
        // 10페이지까지 반복해도 새 게시물이 없는 경우
        throw new NotFoundException(ErrorCode.NOT_FOUND_RESOURCE_EXCEPTION);
    }

    /**
     * 새 글의 유무를 체크한다.
     * @param top2 DB에 저장된 가장 최근 게시물 2개
     * @param titles 페이지에서 가져온 제목들
     * @param dates 페이지에서 가져온 날짜들
     * @return 새 글의 유무
     */
    @Override
    public boolean newNoticeCheck(String[][] top2, List<String> titles, List<String> dates) {
        String firstTitle = top2[0][0]; // 가장 최신 글의 제목
        String firstDate = top2[0][1]; // 가장 최신 글의 날짜
        String secondTitle = top2[1][0]; // 두번째 최신 글의 제목
        String secondDate = top2[1][1]; // 두번째 최신 글의 제목
        boolean hasFirst = false; // 페이지에 가장 최신글의 존재 유무
        boolean hasSecond = false; // 페이지에 두번째 최신글의 존재 유무

        for(int i=0; i<2; i++){
            if(titles.get(i).equals(firstTitle) && dates.get(i).equals(firstDate)) hasFirst = true;
            else if(titles.get(i).equals(secondTitle) && dates.get(i).equals(secondDate)) hasSecond = true;
        }
        return !(hasFirst && hasSecond);
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
