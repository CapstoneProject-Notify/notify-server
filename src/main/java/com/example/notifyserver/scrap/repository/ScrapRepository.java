package com.example.notifyserver.scrap.repository;

import com.example.notifyserver.scrap.domain.Scrap;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    /**
     * 스크랩을 DB에 저장한다.
     * @param scrap 스크랩 객체
     * @return 저장한 스크랩 객체
     */
    @Override
    Scrap save(@NotNull Scrap scrap);

    /**
     * 스크랩을 DB에서 제거한다.
     * @param scrap 제거할 스크랩 객체
     */
    @Override
    void delete(@NotNull Scrap scrap);

    /**
     * 페이지에 맞게 스크랩을 DB에서 가져온다.
     * @param pageable 페이지 정보
     * @return 보여줄 스크랩들
     */
    @Override
    Page<Scrap> findAll(Pageable pageable);
}
