package com.example.notifyserver.crawler.service;

import com.example.notifyserver.common.domain.NoticeType;
import org.springframework.stereotype.Service;

@Service
public interface CrawlerService {

    // 1. 새 글 확인 필요 메서드

    /**
     * 학교 사이트에 로그인 후 게시판 버튼을 클릭하여 게시판 페이지에 진입한다.
     */
    public void loginAndGoToComNoticePage();

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
    public int getNewNoticeCount(NoticeType noticeType);
}
