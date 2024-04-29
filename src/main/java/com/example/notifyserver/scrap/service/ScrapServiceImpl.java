package com.example.notifyserver.scrap.service;

import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.scrap.domain.Scrap;
import com.example.notifyserver.scrap.repository.ScrapRepository;
import com.example.notifyserver.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
@Service
public class ScrapServiceImpl implements ScrapService{

    @Autowired
    private ScrapRepository scrapRepository;

    /**
     * 스크랩 목록에 해당 공지사항을 추가한다.
     * @param user 스크랩을 요청한 유저
     * @param notice 스크랩할 공지사항 객체
     * @return 스크랩한 공지사항의 스크랩 ID
     */
    @Override
    public long doScrap(User user, Notice notice) {
        Scrap scrap = new Scrap(user, notice);
        Scrap savedScrap = scrapRepository.save(scrap);
        return savedScrap.getScrapId();
    }

    /**
     * 스크랩 목록에서 해당 스크랩을 제거한다.
     * @param user 스크랩 제거를 요청한 유저
     * @param notice 스크랩에서 제거할 공지사항 객체
     */
    @Override
    public void deleteScrap(User user, Notice notice) {
        Long userId = user.getUserId();
        Long noticeId = notice.getNoticeId();
        NoticeType noticeType = notice.getNoticeType();
        scrapRepository.deleteByUserIdAndNoticeIdAndType(userId, noticeId, noticeType);
    }

    /**
     * 스크랩 목록에서 해당 페이지의 스크랩을 조회한다.
     * @param userId 스크랩 조회를 요청한 유저의 ID
     * @param pageRequest 조회 페이지 정보
     * @return 페이지 번호에 해당하는 스크랩들
     */
    @Override
    public Page<Scrap> getScrap(long userId, PageRequest pageRequest) {
        return scrapRepository.findAllByUserUserId(userId, pageRequest);
    }
}
