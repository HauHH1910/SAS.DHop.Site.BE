package com.sas.dhop.site.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class ChatController {

    @SendTo("/topic/public")
    @MessageMapping("/chat.send")
    public ChatMessage sendMessage(@Payload ChatMessage message, Principal principal) {
        message.setSender(principal.getName()); // Lấy từ token JWT
        message.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return message;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ChatMessage {
    private String sender;
    private String content;
    private String timestamp;
}