package com.kkm.talkbytag.WebSocket;

import com.kkm.talkbytag.service.PostMessageService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Service
public class PostWebSocketHandler extends TextWebSocketHandler {
    private final PostMessageService postMessageService;

    public PostWebSocketHandler(PostMessageService postMessageService) {
        this.postMessageService = postMessageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        postMessageService.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        postMessageService.removeSession(session);
    }
}
