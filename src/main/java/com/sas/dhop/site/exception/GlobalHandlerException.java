package com.sas.dhop.site.exception;

import com.sas.dhop.site.dto.ResponseError;
import jakarta.validation.ConstraintViolation;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j(topic = "[GLOBAL HANDLER EXCEPTION]")
public class GlobalHandlerException {

    private static final String MIN_ATTRIBUTE = "min";
    private static final String VALUE_ATTRIBUTE = "value";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseError> handleBusinessException(BusinessException e) {
        log.error("[handle business exception] - [{}]", e.getErrorConstant());
        ErrorConstant errorConstant = e.getErrorConstant();
        return ResponseEntity.status(errorConstant.getHttpStatusCode())
                .body(ResponseError.builder()
                        .code(errorConstant.getCode())
                        .message(errorConstant.getMessage())
                        .build());
    }

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<ResponseError> handleIncorrectResultSizeDataAccessException(
            IncorrectResultSizeDataAccessException e) {
        log.info(
                "[IncorrectResultSizeDataAccessException] - [actual size: {}] - [excepted size: {}] - [message: {}]",
                e.getActualSize(),
                e.getExpectedSize(),
                e.getMessage());
        return ResponseEntity.status(ErrorConstant.UNCATEGORIZED_ERROR.getHttpStatusCode())
                .body(ResponseError.builder()
                        .code(ErrorConstant.UNAUTHENTICATED.getCode())
                        .message(ErrorConstant.UNCATEGORIZED_ERROR.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleException(RuntimeException e) {
        log.error("[handle exception] - [{}]", e.getMessage());
        return ResponseEntity.status(ErrorConstant.UNCATEGORIZED_ERROR.getHttpStatusCode())
                .body(ResponseError.builder()
                        .code(ErrorConstant.UNAUTHENTICATED.getCode())
                        .message(ErrorConstant.UNCATEGORIZED_ERROR.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> handleAccessDeniedException(AccessDeniedException e) {
        ErrorConstant errorConstant = ErrorConstant.UNAUTHENTICATED;
        log.error("[handle access denied] - [{}]", e.getMessage());
        return ResponseEntity.status(errorConstant.getHttpStatusCode())
                .body(ResponseError.builder()
                        .code(errorConstant.getCode())
                        .message(errorConstant.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidation(MethodArgumentNotValidException e) {
        String enumKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();

        ErrorConstant errorConstant;

        Map<String, Object> attributes = null;

        try {
            errorConstant = ErrorConstant.valueOf(enumKey);

            var constraintViolation = e.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.error("[handle validation] - [{}]", attributes.toString());
        } catch (IllegalArgumentException exception) {
            throw new RuntimeException(exception);
        }

        return ResponseEntity.badRequest()
                .body(ResponseError.builder()
                        .code(errorConstant.getCode())
                        .message(mapAttribute(errorConstant.getMessage(), attributes))
                        .build());
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        String valueAttribute = String.valueOf(attributes.get(VALUE_ATTRIBUTE));

        if (valueAttribute != null) {
            return message.replace("{" + VALUE_ATTRIBUTE + "}", valueAttribute);
        } else if (minValue != null) {
            return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
        }
        return null;
    }
}
