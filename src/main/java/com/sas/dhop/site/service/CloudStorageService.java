package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.response.ImageResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CloudStorageService {
    List<ImageResponse> uploadImage(MultipartFile[] multipartFiles);
}
