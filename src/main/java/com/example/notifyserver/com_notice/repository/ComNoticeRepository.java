package com.example.notifyserver.com_notice.repository;

import com.example.notifyserver.com_notice.domain.ComNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComNoticeRepository extends JpaRepository<ComNotice, Long> {
    ComNotice save(ComNotice notice);
}
