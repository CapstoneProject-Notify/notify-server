package com.example.notifyserver.common.domain;

import com.example.notifyserver.common.constants.CrawlerConstants;

public enum NoticeType {
    /**
     * 공지 사항 타입
     */
    COM("com", CrawlerConstants.CRAWLING_COM_NOTICE_SIZE_PER_PAGE),    // 공통 공지사항
    BUS("bus", CrawlerConstants.CRAWLING_BUS_NOTICE_SIZE_PER_PAGE),    // 경영학과 공지사항
    COS("cos", CrawlerConstants.CRAWLING_COS_NOTICE_SIZE_PER_PAGE),    // 유학동양학과 공지사항
    AAI("aai", CrawlerConstants.CRAWLING_AAI_NOTICE_SIZE_PER_PAGE),    // 인공지능융합학과 공지사항
    ESM("esm", CrawlerConstants.CRAWLING_ESM_NOTICE_SIZE_PER_PAGE);     // 시스템경영공학과 공지사항

    private final String type;
    private final int noticeSizePerPage; // 한 페이지에 크롤링 해오는 공지사항 개수

    NoticeType(String type, int noticeSizePerPage) {
        this.type = type;
        this.noticeSizePerPage = noticeSizePerPage;
    }

    public String getType() {
        return type;
    }

    public int getNoticeSizePerPage() {
        return noticeSizePerPage;
    }

    /**
     * 사용자로부터 입력받은 문자열로부터 ENUM 타입을 가져온다.
     * @param text 사용자로부터 입력 받은 문자열
     * @return 해당하는 공지사항 종류(ENUM 타입)
     */
    public static NoticeType getTypeFromString(String text) {
        for (NoticeType type : NoticeType.values()) {
            if (type.type.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("주어진 문자열 [" + text +  "] 과 일치하는 타입이 존재하지 않습니다.");
    }


}



