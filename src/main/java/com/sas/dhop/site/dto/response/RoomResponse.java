package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.nosql.Room;

import java.util.List;

public record RoomResponse(List<Room> chatRooms, List<Room> groupRooms) {
}
