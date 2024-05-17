package com.example.notifyserver.common.repository;

import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository<T extends Notice, ID extends Long> extends JpaRepository<Notice, Long> {
    public Optional<Notice> findByNoticeId(Long noticeId);
}
