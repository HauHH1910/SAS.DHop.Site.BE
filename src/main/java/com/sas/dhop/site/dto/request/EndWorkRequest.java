package com.sas.dhop.site.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EndWorkRequest {
    private Integer id;
    private MultipartFile[] multipartFiles;
}
