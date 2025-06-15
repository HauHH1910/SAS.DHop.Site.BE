package com.sas.dhop.site.controller;

import com.sas.dhop.site.constant.ResponseMessage;
import com.sas.dhop.site.dto.ResponseData;
import com.sas.dhop.site.dto.response.MediaResponse;
import com.sas.dhop.site.service.CloudStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
@Tag(name = "[Cloud Storage Controller]")
@Slf4j(topic = "[Cloud Storage Controller]")
public class CloudStorageController {

	private final CloudStorageService cloudStorageService;

	@PostMapping("/images")
	public ResponseData<List<MediaResponse>> uploadImages(@RequestParam("images") MultipartFile[] multipartFiles) {
		return ResponseData.<List<MediaResponse>>builder().message(ResponseMessage.UPLOAD_IMAGE)
				.data(cloudStorageService.uploadImage(multipartFiles)).build();
	}

	@PostMapping("/video")
	public ResponseData<MediaResponse> uploadVideo(@RequestParam("video") MultipartFile multipartFile) {
		return ResponseData.<MediaResponse>builder().message(ResponseMessage.UPLOAD_VIDEO)
				.data(cloudStorageService.uploadVideo(multipartFile)).build();
	}
}
