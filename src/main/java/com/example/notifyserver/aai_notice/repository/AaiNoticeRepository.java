package com.example.notifyserver.aai_notice.repository;

import com.example.notifyserver.aai_notice.domain.AaiNotice;
import com.example.notifyserver.common.repository.NoticeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AaiNoticeRepository extends NoticeRepository<AaiNotice, Long> {
}
