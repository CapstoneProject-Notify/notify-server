package com.example.notifyserver.crawler.service;

import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.crawler.dto.TitlesAndDates;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CrawlerService {

    // 1. 새 글 확인 필요 메서드

    /**
     * 공통 공지사항에 접근하는 경우 학교 사이트에 로그인 후 게시판 버튼을 클릭하여 게시판 페이지에 진입한다.
     * @param driver 크롬 드라이버
     * @param username 로그인 ID
     * @param password 로그인 PW
     */

    public void loginAndGoToComNoticePage(WebDriver driver, String username, String password);

    /**
     * DB에서 가장 최신의 글 2개의 제목과 날짜를 가져온다.
     * @param noticeType 공지사항의 타입
     * @return 제목과 날짜를 매핑한 객체
     */
    public String [][] getLastTwoNotices(NoticeType noticeType);
    
    /**
     * 새 글이 있는지 확인 후 새 글의 개수를 반환한다.
     * @param noticeType 공지사항의 타입
     * @return 새 글의 개수
     */
    public int getNewNoticeCount(NoticeType noticeType, WebDriver driver, String [][] top2);

    /**
     * 공지사항 페이지에서 공지사항의 제목과 날짜를 가져온다.
     * @param driver 크롬 드라이버
     * @return 공지사항 제목과 날짜 리스트
     */
    public TitlesAndDates getTitlesAndDates(WebDriver driver);

    /**
     * 새 글의 개수를 찾는다.
     * @param top2 DB에 저장된 가장 최근 게시물 2개
     * @param driver 크롬 드라이버
     * @return 새 글의 개수
     */
    public int findNewNoticeOrder(String[][] top2, WebDriver driver);

    /**
     * 새 글의 유무를 체크한다.
     * @param top2 DB에 저장된 가장 최근 게시물 2개
     * @param titles 페이지에서 가져온 제목들
     * @param dates 페이지에서 가져온 날짜들
     * @return 새 글의 유무
     */
    public boolean newNoticeCheck(String[][] top2, List<String> titles, List<String> dates);

    /**
     * 날짜 형식을 바꾸는 메서드
     * @param dateString 페이지에서 가져온 날짜 텍스트
     * @return DB에 저장할 날짜 형식을 문자열로 바꾼 값
     */
    public String dateFormating(String dateString);

}
