package com.sas.dhop.site.controller;

import com.sas.dhop.site.dto.payload.MessagePayload;
import com.sas.dhop.site.model.nosql.Message;
import com.sas.dhop.site.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;
	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/sendMessage/{roomId}")
	@SendTo("/topic/room/{roomId}")
	public void sendMessage(@DestinationVariable String roomId, @Payload MessagePayload payload) {
		Message savedMessage = chatService.sendMessage(roomId, payload);
		messagingTemplate.convertAndSend("/topic/room/" + roomId, savedMessage);
	}
}
