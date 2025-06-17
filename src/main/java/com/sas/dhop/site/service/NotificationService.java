package com.sas.dhop.site.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    void addEmitter(SseEmitter emitter);

    void removeEmitter(SseEmitter emitter);

    void sendNotification(String message);

}
