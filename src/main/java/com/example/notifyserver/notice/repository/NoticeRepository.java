package com.example.notifyserver.notice.repository;

import com.example.notifyserver.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository<T extends Notice, ID extends Long> extends JpaRepository<Notice, Long> {
    public Optional<Notice> findByNoticeId(Long noticeId);
}
