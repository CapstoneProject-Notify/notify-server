package com.example.notifyserver.email.service;


import com.example.notifyserver.common.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendNotificationEmail() throws Exception {
        // Given
        MimeMessage mimeMessage = new MimeMessage((jakarta.mail.Session) null); // 실제 MimeMessage 객체 생성
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        String to = "test@example.com";
        String nickname = "tester";
        String keyword = "testKeyword";
        String postLink = "http://localhost:8080/notice/1";

        // When
        emailService.sendNotificationEmail(to, nickname, keyword, postLink);

        // Then
        verify(emailSender).send(mimeMessage);
    }
}


