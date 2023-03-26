package com.kkm.talkbytag.service;

import com.kkm.talkbytag.domain.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topics.newPost}")
    private String newPostTopic;

    public PostEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPostCreatedEvent(Post post) {
        String postMessage = "새 게시글 작성";
                kafkaTemplate.send(newPostTopic, postMessage);
    }
}
