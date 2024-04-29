package com.example.notifyserver.esm_notice.repository;

import com.example.notifyserver.common.repository.NoticeRepository;
import com.example.notifyserver.esm_notice.domain.EsmNotice;
import org.springframework.stereotype.Repository;

@Repository
public interface EsmNoticeRepository extends NoticeRepository<EsmNotice, Long> {
}
