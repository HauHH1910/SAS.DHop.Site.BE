package com.sas.dhop.site.controller;

import com.sas.dhop.site.dto.request.NotificationRequest;
import com.sas.dhop.site.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Tag(name = "[Notification Controller]")
@Slf4j(topic = "[Notification Controller]")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public String sendNotification(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(request.message());
        return "[Sent Notification]" + request.message();
    }


}
