package com.example.notifyserver.com_notice.repository;

import com.example.notifyserver.com_notice.domain.ComNotice;
import com.example.notifyserver.common.repository.NoticeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComNoticeRepository extends NoticeRepository<ComNotice, Long> {
}
