package com.sas.dhop.site.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public record CreateRoomRequest(List<String> participantIds, List<String> names) {
    public String roomName() {
        return String.join("-", names);
    }

    public String roomId() {
        return participantIds.stream()
                .sorted()
                .collect(Collectors.joining("-"));
    }
}
