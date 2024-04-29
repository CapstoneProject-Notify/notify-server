package com.example.notifyserver.cos_notice.repository;

import com.example.notifyserver.common.repository.NoticeRepository;
import com.example.notifyserver.cos_notice.domain.CosNotice;
import org.springframework.stereotype.Repository;

@Repository
public interface CosNoticeRepository extends NoticeRepository<CosNotice, Long> {
}
