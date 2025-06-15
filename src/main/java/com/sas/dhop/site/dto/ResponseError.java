package com.sas.dhop.site.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseError {

	private Integer code;

	private String message;
}
