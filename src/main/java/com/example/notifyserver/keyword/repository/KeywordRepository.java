package com.example.notifyserver.keyword.repository;

import com.example.notifyserver.keyword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    void deleteAllByUserId(Long userId);
}
