package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.request.CreateRoomRequest;
import com.sas.dhop.site.dto.response.RoomResponse;
import com.sas.dhop.site.model.nosql.Message;
import com.sas.dhop.site.model.nosql.Room;
import com.sas.dhop.site.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
@Tag(name = "Room Controller")
@Slf4j(topic = "[Room Controller]")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseData<Room> createRoom(@RequestBody CreateRoomRequest roomId) {
        return ResponseData.<Room>builder()
                .message(ResponseMessage.CREATE_ROOM)
                .data(roomService.createOrGetRoom(roomId))
                .build();
    }

    @GetMapping
    public ResponseData<RoomResponse> getRoom(Principal principal) {
        return ResponseData.<RoomResponse>builder()
                .message(ResponseMessage.GET_ALL_ROOM)
                .data(roomService.getRoom(principal))
                .build();
    }

    @GetMapping("/{roomId}")
    public ResponseData<Room> joinRoom(@PathVariable("roomId") String roomId) {
        return ResponseData.<Room>builder()
                .message(ResponseMessage.JOIN_ROOM)
                .data(roomService.joinRoom(roomId))
                .build();
    }

    @GetMapping("/{roomId}/messages")
    public ResponseData<List<Message>> getMessages(@PathVariable("roomId") String roomId) {
        return ResponseData.<List<Message>>builder()
                .message(ResponseMessage.LIST_MESSAGE)
                .data(roomService.getMessages(roomId))
                .build();
    }
}
