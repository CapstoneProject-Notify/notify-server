package com.example.notifyserver.scrap.service;

import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.scrap.domain.Scrap;
import com.example.notifyserver.scrap.repository.ScrapRepository;
import com.example.notifyserver.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ScrapServiceImplTest {

    @Autowired
    ScrapService scrapService;
    @Autowired
    ScrapRepository scrapRepository;
    @Autowired
    EntityManager em;

    @Test
    void doScrap() {
        //given
        Bundle bundle = getBundle();
        em.persist(bundle.user());
        em.persist(bundle.notice());

        //when
        long findId = scrapService.doScrap(bundle.user(), bundle.notice());
        Scrap scrappedNotice = scrapRepository.findById(findId).orElse(null);

        //then
        assert scrappedNotice != null;
        assertThat(scrappedNotice.getScrapId()).isEqualTo(findId);
    }


    @Test
    public void deleteScrap() throws Exception{
        //given
        Bundle bundle = getBundle();
        em.persist(bundle.user());
        em.persist(bundle.notice());
        scrapService.doScrap(bundle.user, bundle.notice);
        int size = scrapRepository.findAll().size();

        //when
        scrapService.deleteScrap(bundle.user, bundle.notice);

        //then
        assertEquals(size-1, scrapRepository.findAll().size());
    }

    @Test
    public void getScrap() throws Exception{
        //given
        Bundle bundle = getBundle();
        em.persist(bundle.user());
        em.persist(bundle.notice());
        long scrapedId = scrapService.doScrap(bundle.user, bundle.notice);

        //when
        Page<Scrap> scraps = scrapService.getScrap(bundle.user.getUserId(), 1);
        for (Scrap scrap : scraps) {
            assertThat(scrap.getScrapId()).isEqualTo(scrapedId);
        }

        //then
        assertThat(scraps.getTotalElements()).isEqualTo(1);
        assertThat(scraps.getTotalPages()).isEqualTo(1);
    }

    @NotNull
    private static Bundle getBundle() {
        User user = User.builder()
                .googleId("구글아이디")
                .nickName("닉네임")
                .email("이메일")
                .userMajor("bus")
                .build();

        Notice notice = Notice.builder()
                .noticeTitle("공지 제목")
                .noticeType(NoticeType.COM)
                .noticeUrl("공지 url")
                .build();
        return new Bundle(user, notice);
    }

    private record Bundle(User user, Notice notice) {
    }
}
