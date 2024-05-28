package com.example.notifyserver.scrap.service;

import com.example.notifyserver.common.constants.NoticeConstants;
import com.example.notifyserver.notice.domain.Notice;
import com.example.notifyserver.common.domain.NoticeType;
import com.example.notifyserver.scrap.domain.Scrap;
import com.example.notifyserver.scrap.repository.ScrapRepository;
import com.example.notifyserver.user.domain.User;
import com.example.notifyserver.user.domain.UserMajor;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        PageRequest pr = PageRequest.of(0, (int) NoticeConstants.PAGE_SIZE);

        //when
        Page<Scrap> scrapsWithPage = scrapService.getScrap(bundle.user.getUserId(), pr);
        List<Scrap> scrapList = scrapsWithPage.getContent();

        //then
        assert(scrapList.size() <= NoticeConstants.PAGE_SIZE);
        assertThat(scrapsWithPage.getNumber()).isEqualTo(0);
    }

    @NotNull
    private static Bundle getBundle() {
        User user = User.builder()
                .googleId("구글아이디")
                .nickName("닉네임")
                .email("이메일")
                .userMajor(UserMajor.BUS)
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
