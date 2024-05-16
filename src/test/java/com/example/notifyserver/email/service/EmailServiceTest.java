package com.example.notifyserver.email.service;

import com.example.notifyserver.common.service.EmailService;
import jakarta.mail.Session;
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

import javax.mail.MessagingException;

import static org.mockito.ArgumentMatchers.any;
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
    public void testSendNotificationEmail() throws MessagingException {
        // Given
        String to = "test@example.com";
        String nickname = "tester";
        String keyword = "testKeyword";
        String postLink = "http://example.com/post/1";

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        emailService.sendNotificationEmail(to, nickname, keyword, postLink);

        // Then
        verify(emailSender).send(any(MimeMessage.class));
    }

    @Test
    public void testSendEmail() throws MessagingException {
        // Given
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        emailService.sendEmail(to, subject, text);

        // Then
        verify(emailSender).send(any(MimeMessage.class));
    }
}

