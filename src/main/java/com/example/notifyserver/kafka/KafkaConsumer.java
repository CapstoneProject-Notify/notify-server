package com.example.notifyserver.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "notify-crawler-topic", groupId = "notify")
    public void listen(String message) {
        log.info(message + "from Kafka");
    }
}