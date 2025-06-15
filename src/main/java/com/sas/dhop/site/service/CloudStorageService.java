package com.sas.dhop.site.service;

import com.sas.dhop.site.dto.response.MediaResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CloudStorageService {
  List<MediaResponse> uploadImage(MultipartFile[] multipartFiles);

  MediaResponse uploadVideo(MultipartFile multipartFile);
}
