package com.sas.dhop.site.service.impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.sas.dhop.site.dto.response.ImageResponse;
import com.sas.dhop.site.service.CloudStorageService;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Cloud Storage Service]")
public class CloudStorageServiceImpl implements CloudStorageService {

    @Value("${sas.firebase-bucket}")
    private String bucketName;

    @Value("${sas.firebase-key}")
    private String firebaseKey;

    @Override
    public List<ImageResponse> uploadImage(MultipartFile[] multipartFiles) {
        List<ImageResponse> imageResponses = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                String fileName =
                        UUID.randomUUID().toString().concat(this.getExtension(multipartFile.getOriginalFilename()));

                log.info("Uploading file: {}", fileName);

                File file = this.convertToFile(multipartFile, fileName);

                String url = this.uploadFile(file, fileName);

                deleteFile(file);

                imageResponses.add(new ImageResponse(url));

                log.info("File uploaded successfully: {}", url);

                if (imageResponses.size() > 3) {
                    break;
                }
            } catch (IOException e) {
                log.error("Error uploading file", e);
                throw new RuntimeException(e);
            }
        }
        return imageResponses;
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        Credentials credentials =
                GoogleCredentials.fromStream(new ByteArrayInputStream(firebaseKey.getBytes(StandardCharsets.UTF_8)));

        Storage storage =
                StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucketName, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
            fileOutputStream.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private void deleteFile(File file) {
        boolean deleted = file.delete();
        if (deleted) {
            log.info("Temporary file {} deleted successfully.", file.getName());
        } else {
            log.warn("Failed to delete temporary file {}", file.getName());
        }
    }
}
