package com.example.notifyserver.keyword.repository;

import com.example.notifyserver.keyword.domain.Keyword;
import com.example.notifyserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    void deleteAllByUser(User user);
}
