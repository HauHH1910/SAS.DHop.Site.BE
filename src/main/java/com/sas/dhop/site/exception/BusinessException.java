package com.sas.dhop.site.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorConstant errorConstant;

    public BusinessException(final ErrorConstant errorConstant) {
        super(errorConstant.getMessage());
        this.errorConstant = errorConstant;
    }

}
