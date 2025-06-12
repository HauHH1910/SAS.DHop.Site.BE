package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.exception.BusinessException;
import com.sas.dhop.site.exception.ErrorConstant;
import com.sas.dhop.site.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static jakarta.persistence.GenerationType.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Cloud Storage Service]")
public class CloudStorageServiceImpl implements CloudStorageService {
    @Override
    public String uploadImage(List<MultipartFile> multipartFiles) {
        if (multipartFiles.size() > 3) {
            //Remove last image if upload images if larger than 3
            multipartFiles.remove(multipartFiles.get(multipartFiles.size() - 1));
        }
        return "";
    }

//
}
