package com.sas.dhop.site.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ComplainResponse(
        String content,
        String complainPersonName,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime complainTime
        ) {
}
