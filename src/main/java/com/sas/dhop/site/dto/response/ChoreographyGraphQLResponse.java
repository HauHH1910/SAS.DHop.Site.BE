package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.Choreography;
import com.sas.dhop.site.model.DanceType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record ChoreographyGraphQLResponse(
        Integer id,
        Set<String> danceTypeNames,
        UserGraphQLResponse user,
        String about,
        Integer yearExperience,
        BigDecimal price,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ChoreographyGraphQLResponse mapToChoreographyGraphQLResponse(Choreography choreography) {
        return ChoreographyGraphQLResponse.builder()
                .id(choreography.getId())
                .danceTypeNames(mapDanceTypeToNames(choreography.getDanceTypes()))
                .user(UserGraphQLResponse.mapToUserGraphQLResponse(choreography.getUser()))
                .about(choreography.getAbout())
                .yearExperience(choreography.getYearExperience())
                .price(choreography.getPrice())
                .status(choreography.getStatus().getStatusName())
                .createdAt(choreography.getCreatedAt())
                .updatedAt(choreography.getUpdatedAt())
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