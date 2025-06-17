package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserGraphQLResponse(
        Integer id,
        String avatar,
        Integer age,
        String name,
        String email,
        String phone,
        AreaResponse area,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static UserGraphQLResponse mapToUserGraphQLResponse(User user) {
        return UserGraphQLResponse.builder()
                .id(user.getId())
                .avatar(user.getAvatar())
                .age(user.getAge())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .area(user.getArea() != null ? AreaResponse.mapToAreaResponse(user.getArea()) : null)
                .status(user.getStatus().getStatusName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
} 