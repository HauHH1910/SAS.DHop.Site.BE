package com.sas.dhop.site.dto.response;

import com.sas.dhop.site.model.Performance;

public record PerformanceResponse(Integer id, Integer userId, String username, String mediaUrl) {

  public static PerformanceResponse mapToPerformance(Performance request) {
    return new PerformanceResponse(
        request.getId(),
        request.getUser().getId(),
        request.getUser().getName(),
        request.getMediaUrl());
  }
}
