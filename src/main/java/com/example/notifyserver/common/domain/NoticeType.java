package com.example.notifyserver.common.domain;

public enum NoticeType {
    /**
     * 공지 사항 타입
     */
    COM("com"),    // 공통 공지사항
    BUS("bus"),    // 경영학과 공지사항
    COS("cos"),    // 유학동양학과 공지사항
    AAI("aai"),    // 인공지능융합학과 공지사항
    ESM("esm");     // 시스템경영공학과 공지사항

    private final String type;

    NoticeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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



