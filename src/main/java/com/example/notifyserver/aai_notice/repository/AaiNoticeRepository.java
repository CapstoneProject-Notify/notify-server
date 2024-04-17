package com.example.notifyserver.aai_notice.repository;

import com.example.notifyserver.aai_notice.domain.AaiNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AaiNoticeRepository extends JpaRepository<AaiNotice, Long > {
}
