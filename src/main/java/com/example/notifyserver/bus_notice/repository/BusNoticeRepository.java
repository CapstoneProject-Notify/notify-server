package com.example.notifyserver.bus_notice.repository;

import com.example.notifyserver.bus_notice.domain.BusNotice;
import com.example.notifyserver.common.repository.NoticeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusNoticeRepository extends NoticeRepository<BusNotice, Long> {
}
