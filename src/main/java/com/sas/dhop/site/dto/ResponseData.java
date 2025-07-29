package com.sas.dhop.site.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ResponseData<T> {

    @Builder.Default
    private int code = 200;

    private String message;

    private T data;

    public static <T> ResponseData<T> created(T data, String message) {
        return ResponseData.<T>builder()
                .code(HttpStatus.CREATED.value())
                .message(message)
                .data(data)
                .build();
    }

    public static ResponseData<Void> noContent(String message) {
        return ResponseData.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message(message)
                .build();
    }

    public static <T> ResponseData<T> ok(T data, String message) {
        return ResponseData.<T>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }
}
