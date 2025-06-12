package com.sas.dhop.site.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ArticleRequest(
        String title,
        String content,
        String authorName,
        MultipartFile[] thumbnail
) {
}
