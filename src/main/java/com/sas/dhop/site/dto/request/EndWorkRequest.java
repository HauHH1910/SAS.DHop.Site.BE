package com.sas.dhop.site.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record EndWorkRequest(Integer id, MultipartFile[] multipartFiles) {
}
