package com.kkm.talkbytag.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import org.springframework.kafka.annotation.KafkaListener;

@Service
public class PostEventConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PostMessageService postMessageService;

    public PostEventConsumer(KafkaTemplate<String, String> kafkaTemplate, PostMessageService postMessageService) {
        this.kafkaTemplate = kafkaTemplate;
        this.postMessageService = postMessageService;
    }

    @KafkaListener(topics = "post_events", groupId = "post_event_group")
    public void consume(String postMessage) {
        System.out.println("Received Post Event: " + postMessage);
        sendPostMessageToClients(postMessage);
    }

    private void sendPostMessageToClients(String postMessage) {
        postMessageService.broadcastMessage(postMessage);
    }
}
