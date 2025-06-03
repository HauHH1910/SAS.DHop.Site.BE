package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.CreateRoomRequest;
import com.sas.dhop.site.dto.response.RoomResponse;
import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.model.User;
import com.sas.dhop.site.model.nosql.Message;
import com.sas.dhop.site.model.nosql.Room;
import com.sas.dhop.site.repository.UserRepository;
import com.sas.dhop.site.repository.nosql.RoomRepository;
import com.sas.dhop.site.service.RoomService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Room Service]")
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public Room createOrGetRoom(CreateRoomRequest request) {
        return roomRepository.findByRoomId(request.roomId()).orElseGet(() -> {
            log.info("[Room {} by {} are being created sir]", request.roomId(), request.roomName());
            return roomRepository.save(Room.builder()
                    .roomId(request.roomId())
                    .name(request.roomName())
                    .build());
        });
    }

    @Override
    public List<Message> getMessages(String roomId) {
        return roomRepository
                .findByRoomId(roomId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.ROOM_NOT_FOUND))
                .getMessages();
    }

    @Override
    public Room joinRoom(String roomId) {
        return roomRepository
                .findByRoomId(roomId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.ROOM_NOT_FOUND));
    }

    @Override
    public RoomResponse getRoom(Principal principal) {
        User user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));
        log.info("Find room with ID: [{}] ", user.getId());
        List<Room> rooms = roomRepository.findByRoomIdContaining(user.getId().toString());

        List<Room> chatRooms = rooms.stream()
                .filter(room -> room.getRoomId().split("-").length == 2)
                .toList();

        List<Room> groupRooms = rooms.stream()
                .filter(room -> room.getRoomId().split("-").length > 2)
                .toList();
        return new RoomResponse(chatRooms, groupRooms);
    }
}
