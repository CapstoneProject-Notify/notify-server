package com.example.notifyserver.kafka;

import com.example.notifyserver.notice.domain.Notice;
import com.example.notifyserver.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaConsumer {
    @Autowired
    UserService userService;

    @KafkaListener(topics = "notify-crawler-topic", groupId = "notify")
    public void consume(List<Notice> notices) {
        if (notices == null || notices.isEmpty()) {
            // 비어있는 noticeList가 오는 경우에 대한 처리
            log.info("================ 새롭게 크롤링 된 게시물이 없습니다. ================");
            return;
        }

        // Kafka 메시지를 역직렬화한 List<Notice> 객체를 사용하여 원하는 작업을 수행
        for (Notice notice : notices) {
            userService.findAndSendEmail(notice);
        }
        log.info("================ 크롤링 된 게시물 이메일 작업 완료. ================");
    }
}
