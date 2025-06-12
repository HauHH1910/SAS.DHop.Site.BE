package com.sas.dhop.site.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudStorageService {
    String uploadImage(List<MultipartFile> multipartFiles);
}
