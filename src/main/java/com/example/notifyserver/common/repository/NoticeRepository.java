package com.example.notifyserver.common.repository;

import com.example.notifyserver.common.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository<T extends Notice, ID extends Long> extends JpaRepository<Notice, Long> {
}
