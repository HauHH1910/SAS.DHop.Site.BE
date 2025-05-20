package com.sas.dhop.site.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseData<T> {

    @Builder.Default
    private int code = 200;

    private String message;

    private T data;
}
