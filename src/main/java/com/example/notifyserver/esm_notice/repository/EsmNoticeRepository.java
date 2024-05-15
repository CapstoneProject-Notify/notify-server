package com.example.notifyserver.esm_notice.repository;

import com.example.notifyserver.esm_notice.domain.EsmNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsmNoticeRepository extends JpaRepository<EsmNotice, Long> {
    EsmNotice save(EsmNotice notice);
}
