package com.example.notifyserver.user.service;

import com.example.notifyserver.common.domain.Notice;
import com.example.notifyserver.common.service.EmailService;
import com.example.notifyserver.keyword.domain.Keyword;
import com.example.notifyserver.keyword.repository.KeywordRepository;
import com.example.notifyserver.user.domain.User;
import com.example.notifyserver.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private KeywordRepository keywordRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Mockito 애노테이션 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAndSendEmail() {
        // 테스트 데이터 설정
        Notice notice = new Notice();
        notice.setNoticeTitle("This is a test notice with keyword");
        notice.setNoticeId(1L);

        Keyword keyword1 = new Keyword();
        keyword1.setUserKeyword("keyword");
        User user1 = new User();
        user1.setUserId(1L);
        user1.setEmail("user1@example.com");
        user1.setNickName("User1");
        keyword1.setUser(user1);

        Keyword keyword2 = new Keyword();
        keyword2.setUserKeyword("test");
        User user2 = new User();
        user2.setUserId(2L);
        user2.setEmail("user2@example.com");
        user2.setNickName("User2");
        keyword2.setUser(user2);

        List<Keyword> allKeywords = Arrays.asList(keyword1, keyword2);

        when(keywordRepository.findAll()).thenReturn(allKeywords);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        // 테스트할 메서드 호출
        userService.findAndSendEmail(notice);

        // 이메일이 올바르게 전송되었는지 확인
        verify(emailService).sendNotificationEmail("user1@example.com", "User1", "keyword", "http://localhost:8080/notice/1");
        verify(emailService).sendNotificationEmail("user2@example.com", "User2", "test", "http://localhost:8080/notice/1");
    }
}


