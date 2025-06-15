package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.dto.request.CreateRoomRequest;
import com.sas.dhop.site.dto.response.RoomDetailResponse;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public RoomDetailResponse joinRoom(String roomId) {
        Room room = roomRepository
                .findByRoomId(roomId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.ROOM_NOT_FOUND));

        String[] split = room.getRoomId().split("-");
        List<Integer> participantIds =
                Arrays.stream(split).map(Integer::parseInt).toList();

        User currentUser = userRepository
                .findByEmail(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));

        Integer otherParticipantId = participantIds.stream()
                .filter(participantId -> !participantId.equals(currentUser.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorConstant.PARTICIPANT_NOT_FOUND));

        User anotherParticipant = userRepository
                .findById(otherParticipantId)
                .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

        String lastMessageContent = "Không có tin nhắn";

        if (!room.getMessages().isEmpty()) {
            lastMessageContent =
                    room.getMessages().get(room.getMessages().size() - 1).getContent();
        }

        return new RoomDetailResponse(
                anotherParticipant.getName(),
                room.getName(),
                anotherParticipant.getAvatar(),
                room.getRoomId(),
                lastMessageContent);
    }

    @Override
    public RoomResponse getRoom(Principal principal) {
        User user = userRepository
                .findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException(ErrorConstant.EMAIL_NOT_FOUND));
        log.info("Find room with ID: [{}] ", user.getId());

        List<Room> rooms = roomRepository.findByRoomIdContaining(user.getId().toString());

        List<RoomDetailResponse> chatRooms = new ArrayList<>();
        List<RoomDetailResponse> groupRooms = new ArrayList<>();

        rooms.forEach(room -> {
            String[] split = room.getRoomId().split("-");
            List<Integer> participantIds =
                    Arrays.stream(split).map(Integer::parseInt).toList();

            if (participantIds.size() == 2) {
                Integer otherParticipantId = participantIds.stream()
                        .filter(id -> !id.equals(user.getId()))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(ErrorConstant.PARTICIPANT_NOT_FOUND));

                User otherParticipant = userRepository
                        .findById(otherParticipantId)
                        .orElseThrow(() -> new BusinessException(ErrorConstant.USER_NOT_FOUND));

                String lastMessage = (room.getMessages().isEmpty())
                        ? "Không có tin nhắn"
                        : room.getMessages().get(room.getMessages().size() - 1).getContent();

                chatRooms.add(new RoomDetailResponse(
                        otherParticipant.getName(),
                        room.getName(),
                        otherParticipant.getAvatar(),
                        room.getRoomId(),
                        lastMessage));
            } else if (participantIds.size() > 2) {
                String lastMessage = (room.getMessages().isEmpty())
                        ? "Không có tin nhắn"
                        : room.getMessages().get(room.getMessages().size() - 1).getContent();

                groupRooms.add(new RoomDetailResponse(
                        room.getName(), room.getName(), "logo-icon.png", room.getRoomId(), lastMessage));
            }
        });

        return new RoomResponse(chatRooms, groupRooms);
    }
}
