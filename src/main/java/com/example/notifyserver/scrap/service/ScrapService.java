package com.example.notifyserver.scrap.service;

import com.example.notifyserver.scrap.domain.Scrap;

import java.util.List;

public interface ScrapService {
    /**
     * 스크랩 목록에 해당 공지사항을 추가한다.
     * @param userId 스크랩을 요청한 유저의 ID
     * @param type 공지사항의 종류
     * @param noticeId 스크랩할 공지사항의 ID
     * @return 스크랩한 공지사항의 ID
     */
    long doScrap(long userId, String type, long noticeId);

    /**
     * 스크랩 목록에서 해당 스크랩을 제거한다.
     * @param userId 스크랩 제거를 요청한 유저의 ID
     * @param type 공지사항의 종류
     * @param noticeId 스크랩에서 제거할 공지사항의 ID
     * @return 제거한 공지사항의 ID
     */
    long deleteScrap(long userId, String type, long noticeId);

    /**
     * 스크랩 목록에서 해당 페이지의 스크랩을 조회한다.
     * @param userId 스크랩 조회를 요청한 유저의 ID
     * @param pageNum 조회 페이지 번호
     * @return 페이지 번호에 해당하는 스크랩 리스트
     */
    List<Scrap> getScrap(long userId, long pageNum);
}
