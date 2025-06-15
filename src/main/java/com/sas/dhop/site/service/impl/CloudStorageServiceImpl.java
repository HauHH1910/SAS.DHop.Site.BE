package com.sas.dhop.site.service.impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.sas.dhop.site.dto.response.MediaResponse;
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
  public List<MediaResponse> uploadImage(MultipartFile[] multipartFiles) {
    List<MediaResponse> responses = new ArrayList<>();
    for (MultipartFile multipartFile : multipartFiles) {
      try {
        String fileName = generateUniqueFilename(multipartFile);

        log.info("Uploading file: {}", fileName);

        File file = convertToFile(multipartFile, fileName);

        String url = uploadToFirebase(file, fileName, multipartFile.getContentType());

        deleteFile(file);

        responses.add(new MediaResponse(url));

        log.info("File uploaded successfully: {}", url);

        if (responses.size() > 3) {
          break;
        }
      } catch (IOException e) {
        log.error("Error uploading file", e);
        throw new RuntimeException(e);
      }
    }
    return responses;
  }

  @Override
  public MediaResponse uploadVideo(MultipartFile multipartFile) {
    try {
      String fileName = generateUniqueFilename(multipartFile);
      log.info("Uploading video: {}", fileName);

      File file = convertToFile(multipartFile, fileName);

      String url = uploadToFirebase(file, fileName, multipartFile.getContentType());

      deleteFile(file);

      return new MediaResponse(url);
    } catch (Exception e) {
      log.error("Failed to upload video", e);
      throw new RuntimeException("Video upload failed", e);
    }
  }

  private String uploadToFirebase(File file, String fileName, String contentType)
      throws IOException {
    BlobId blobId = BlobId.of(bucketName, fileName);
    BlobInfo blobInfo =
        BlobInfo.newBuilder(blobId)
            .setContentType(
                contentType != null ? contentType : Files.probeContentType(file.toPath()))
            .build();

    Credentials credentials =
        GoogleCredentials.fromStream(
            new ByteArrayInputStream(firebaseKey.getBytes(StandardCharsets.UTF_8)));

    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

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

  private String generateUniqueFilename(MultipartFile multipartFile) {
    return UUID.randomUUID() + getExtension(multipartFile.getOriginalFilename());
  }
}
