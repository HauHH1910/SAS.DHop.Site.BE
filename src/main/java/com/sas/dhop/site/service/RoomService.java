package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.request.CreateRoomRequest;
import com.sas.dhop.site.dto.response.RoomDetailResponse;
import com.sas.dhop.site.dto.response.RoomResponse;
import com.sas.dhop.site.model.nosql.Message;
import com.sas.dhop.site.model.nosql.Room;
import java.security.Principal;
import java.util.List;

public interface RoomService {

	Room createOrGetRoom(CreateRoomRequest roomId);

	RoomDetailResponse joinRoom(String roomId);

	List<Message> getMessages(String roomId);

	RoomResponse getRoom(Principal principal);
}
