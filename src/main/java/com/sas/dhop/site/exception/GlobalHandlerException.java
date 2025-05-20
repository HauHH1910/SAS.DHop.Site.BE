package com.sas.dhop.site.exception;

import com.sas.dhop.site.common.ResponseError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j(topic = "[GLOBAL HANDLER EXCEPTION]")
public class GlobalHandlerException {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseError> handleBusinessException(BusinessException e) {
        ErrorConstant errorConstant = e.getErrorConstant();
        return ResponseEntity.status(errorConstant.getHttpStatusCode())
                .body(ResponseError.builder()
                        .code(errorConstant.getCode())
                        .message(errorConstant.getMessage())
                        .build());
    }

}
