package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.Dancer;
import com.sas.dhop.site.model.DanceType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record DancerGraphQLResponse(
        Integer id,
        String dancerNickName,
        Set<String> danceTypeName,
        UserGraphQLResponse user,
        String about,
        Integer yearExperience,
        Integer teamSize,
        BigDecimal price,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DancerGraphQLResponse mapToDancerGraphQLResponse(Dancer dancer) {
        return DancerGraphQLResponse.builder()
                .id(dancer.getId())
                .dancerNickName(dancer.getDancerNickName())
                .danceTypeName(mapDanceTypeToNames(dancer.getDanceTypes()))
                .user(UserGraphQLResponse.mapToUserGraphQLResponse(dancer.getUser()))
                .about(dancer.getAbout())
                .yearExperience(dancer.getYearExperience())
                .teamSize(dancer.getTeamSize())
                .price(dancer.getPrice())
                .status(dancer.getStatus().getStatusName())
                .createdAt(dancer.getCreatedAt())
                .updatedAt(dancer.getUpdatedAt())
                .build();
    }

    private static Set<String> mapDanceTypeToNames(Set<DanceType> danceTypes) {
        if (danceTypes == null) {
            return Collections.emptySet();
        }
        return danceTypes.stream()
                .map(DanceType::getType)
                .collect(Collectors.toSet());
    }
} 