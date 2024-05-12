package com.example.notifyserver.crawler.service;

import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.crawler.dto.TitlesAndDates;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
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

    WebDriver loginAndGoToComNoticePage(WebDriver driver, String username, String password);

    /**
     * DB에서 가장 최신의 글 2개의 제목과 날짜를 가져온다.
     * @param noticeType 공지사항의 타입
     * @return 제목과 날짜를 매핑한 객체
     */
    String [][] getLastTwoNotices(NoticeType noticeType);
    
    /**
     * 새 글이 있는지 확인 후 새 글의 개수를 반환한다.
     * @param noticeType 공지사항의 타입
     * @return 새 글의 개수
     */
    int getNewNoticeCount(NoticeType noticeType, WebDriver driver, String[][] top2) throws InterruptedException, ParseException;

    /**
     * 공지사항 페이지에서 공통 공지사항의 제목과 날짜를 가져온다.
     * @param driver 크롬 드라이버
     * @return 공지사항 제목과 날짜 리스트
     */
    TitlesAndDates getTitlesAndDatesOfComNoticeFromPageNum(WebDriver driver, int pageNum) throws ParseException;

    /**
     * 공통 공지사항의 새 글의 개수를 찾는다.
     * @param top2 DB에 저장된 가장 최근 게시물 2개
     * @param driver 크롬 드라이버
     * @return 새 글의 개수
     */
    int findNewNoticeOrder(String[][] top2, WebDriver driver) throws InterruptedException, ParseException;

    /**
     * 새 글의 유무를 체크한다.
     * @param top2 DB에 저장된 가장 최근 게시물 2개
     * @param titles 페이지에서 가져온 제목들
     * @param dates 페이지에서 가져온 날짜들
     * @return 새 글의 유무
     */
    boolean newComNoticeCheck(String[][] top2, List<String> titles, List<Date> dates);

    /**
     * 해당 페이지 번호에서 공지사항들을 가져옵니다.
     * @param username 로그인 ID
     * @param password 로그인 PW
     * @param pageNum 가져올 페이지 번호
     * @param driver 크롬 드라이버
     * @return 해당 페이지 번호의 공지사항들 리스트
     * @throws InterruptedException
     * @throws ParseException
     */
    List<Notice> getNewNoticesByPageNum(String username, String password, int pageNum, WebDriver driver) throws InterruptedException, ParseException;

    /**
     * 페이지에서 가져온 공통 공지사항들의 날짜 텍스트를 Date 객체로 변환한다.
     * @param input 페이지에서 가져온 공통 공지사항의 날짜 텍스트
     * @return Date 객체로 변환한 공통 공지사항의 날짜
     * @throws ParseException
     */
    Date parseComNoticeDateAndFormatting(String input) throws ParseException;

}
