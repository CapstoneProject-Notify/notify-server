package com.example.notifyserver.crawler.controller;

import com.example.notifyserver.common.constants.CrawlerConstants;
import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.crawler.service.CrawlerService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class CrawlerController {

    @Autowired
    CrawlerService crawlerService;

    /**
     * 공통 공지사항의 새 글을 가져와 DB에 저장한다.
     */
    public void getComNotice() throws InterruptedException, ParseException {
        // Headless 모드로 Chrome 실행
        ChromeOptions options = new ChromeOptions();
        // Headless 모드 활성화
        options.addArguments("--headless");
        // WebDriver 인스턴스 생성
        WebDriver driver = new ChromeDriver(options);

        // 로그인 아이디와 비밀번호 디코딩
        String username = new String(Base64.getDecoder().decode("YWxlbmpi"));
        String password = new String(Base64.getDecoder().decode("ZGx3amRxbHMxMiE="));

        crawlerService.loginAndGoToComNoticePage(driver, username, password);
        // 새 글의 개수
        int newNoticeCount = crawlerService.getNewNoticeCount(
                NoticeType.COM,
                driver,
                crawlerService.getLastTwoNotices(NoticeType.COM));
            driver.quit();

        // 새 글이 있으면
        if(newNoticeCount > 0){
            // 새 글들을 저장할 리스트
            List<Notice> newNotices = new ArrayList<>();

            // 새 글을 크롤링할 페이지 수
            int newNoticePageCount = newNoticeCount / CrawlerConstants.CRAWLING_COM_NOTICE_SIZE_PER_PAGE;
            // 새 글 수를 페이지 수로 나누었을 때 떨어지는 나머지 값
            int newNoticeNumInLastPage = newNoticeCount % CrawlerConstants.CRAWLING_COM_NOTICE_SIZE_PER_PAGE;

            // 페이지에서 일부만 크롤링해야하는 새 글들을 크롤링 해오기
            List<Notice> newNoticesFromLastPage = crawlerService.getNewNoticesByPageNum(username, password, newNoticePageCount + 1, driver);
            for(int i=1; i<=newNoticeNumInLastPage; i++){
                Notice notice = newNoticesFromLastPage.get(CrawlerConstants.CRAWLING_COM_NOTICE_SIZE_PER_PAGE - i);
                newNotices.add(notice);
            }
            // 페이지를 통채로 가져올 수 있는 만큼 새 글을 크롤링 해오기
            for(int i=newNoticePageCount; i>=1 ;i--){
                List<Notice> notices = crawlerService.getNewNoticesByPageNum(username, password, i, driver);
                for (Notice notice : notices) {
                    newNotices.add(notice);
                }
            }
            // 가져온 새 공통 공지사항들 DB에 저장
            crawlerService.saveNewComNotices(newNotices);
        }
    }
}