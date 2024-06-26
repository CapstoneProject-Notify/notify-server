package com.example.notifyserver.scrap.repository;

import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.scrap.domain.Scrap;
import com.example.notifyserver.user.domain.User;
import feign.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    void deleteAllByUser(User user);

    /**
     * 스크랩을 DB에 저장한다.
     * @param scrap 스크랩 객체
     * @return 저장한 스크랩 객체
     */
    @Transactional
    @Override
    Scrap save(@NotNull Scrap scrap);


    /**
     * 스크랩을 DB에서 제거한다.
     * @param userId 제거할 스크랩의 소유 회원
     * @param noticeId 제거할 공지사항의 ID
     * @param noticeType 제거할 공지사항의 종류
     */

    @Transactional
    @Modifying
    @Query("DELETE FROM Scrap s WHERE s.user.userId = :userId AND s.notice.noticeId = :noticeId AND s.notice.noticeType = :noticeType")
    void deleteByUserIdAndNoticeIdAndType(@Param("userId") Long userId, @Param("noticeId") Long noticeId, @Param("noticeType") NoticeType noticeType);

    /**
     * 페이지에 맞게 스크랩을 DB에서 가져온다.
     * @param userId 조회할 스크랩의 소유 회원 아이디
     * @param pageable 페이지 정보
     * @return 보여줄 스크랩들 및 페이지 정보
     */
    @Transactional
    Page<Scrap> findAllByUserUserIdOrderByNoticeDesc(long userId, Pageable pageable);
    @Query("SELECT s FROM Scrap s WHERE s.user.userId = :userId AND s.notice.noticeId = :noticeId AND s.notice.noticeType = :noticeType")
    Scrap findAllByUserIdAndNoticeIdAndType(@Param("userId") Long userId, @Param("noticeId") Long noticeId, @Param("noticeType") NoticeType noticeType);

    /**
     * 공지사항이 특정 사용자에 의해 스크랩되었는지 확인한다.
     * @param userId 사용자 아이디
     * @param noticeId 공지사항 아이디
     * @param type 공지사항 종류
     * @return 특정 사용자의 해당 공지사항 스크랩 여부
     */
    @Query("SELECT COUNT(s) > 0 FROM Scrap s WHERE s.user.userId = :userId AND s.notice.noticeId = :noticeId AND s.notice.noticeType = :type")
    boolean existsByUserIdAndNoticeIdAndType(@Param("userId") Long userId, @Param("noticeId") Long noticeId, @Param("type") NoticeType type);
}
