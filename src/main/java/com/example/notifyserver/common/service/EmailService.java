package com.example.notifyserver.common.service;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class EmailService {

    private JavaMailSender emailSender;

    public void sendNotificationEmail(String to, String nickname, String keyword, String postLink) {
        String subject = "키워드 " + keyword + "에 대한 새로운 알림";
        String content = String.format(
                "%s님이 설정하신 키워드 %s와 관련된 새 게시물이 등록되었습니다. " +
                        "아래 링크에서 게시물을 확인하세요.<br><a href=\"%s\">%s</a>",
                nickname, keyword, postLink, postLink
        );

        sendEmail(to, subject, content);
    }

    public void sendEmail(String to, String subject, String text) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);  // 두 번째 파라미터를 true로 설정하여 HTML로 전송
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        emailSender.send(mimeMessage);
    }
}
