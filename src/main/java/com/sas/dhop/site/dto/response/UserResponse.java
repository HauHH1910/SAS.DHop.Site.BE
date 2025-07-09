package com.sas.dhop.site.dto.response;

public record UserResponse(Integer id, String avatar, String name, String email, String phone, AreaResponse areaResponse) {}
