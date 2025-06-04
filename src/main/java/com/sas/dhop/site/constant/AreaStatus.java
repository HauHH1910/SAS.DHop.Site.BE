package com.sas.dhop.site.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AreaStatus {
    ACTIVATED_AREA("Khu vực được cấp phép hoạt động"),
    INACTIVE_AREA("Khu vực không được cấp phép hoạt động");

    private final String name;

}
