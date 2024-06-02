package com.example.notifyserver.kafka;

import com.example.notifyserver.notice.domain.Notice;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.List;

public class NoticeListDeserializer implements Deserializer<List<Notice>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Notice> deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, new TypeReference<List<Notice>>() {});
        } catch (Exception e) {
            throw new RuntimeException(" List<Noitce> 역직렬화 오류 ", e);
        }
    }
}