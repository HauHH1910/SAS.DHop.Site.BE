package com.sas.dhop.site.dto.response;

import java.util.List;

public record RoomResponse(
    List<RoomDetailResponse> chatRooms, List<RoomDetailResponse> groupRooms) {}
