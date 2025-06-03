package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.payload.MessagePayload;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.nosql.Message;
import com.sas.dhop.site.model.nosql.Room;
import com.sas.dhop.site.repository.nosql.RoomRepository;
import com.sas.dhop.site.service.ChatService;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Chat Service]")
public class ChatServiceImpl implements ChatService {

    private final RoomRepository roomRepository;

    @Override
    public Message sendMessage(String roomId, MessagePayload payload) {
        Room room = roomRepository
                .findByRoomId(roomId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.ROOM_NOT_FOUND));
        Message message = Message.builder()
                .content(payload.getContent())
                .sender(payload.getSender())
                .timeStamp(LocalDateTime.now())
                .build();

        if (room != null) {
            room.getMessages().add(message);
            roomRepository.save(room);
        } else {
            throw new BusinessException(ErrorConstant.ROOM_NOT_FOUND);
        }
        return message;
    }
}
