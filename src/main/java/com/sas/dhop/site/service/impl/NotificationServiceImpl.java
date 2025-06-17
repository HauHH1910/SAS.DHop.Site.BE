package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.service.NotificationService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Notification Service]")
public class NotificationServiceImpl implements NotificationService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    public void addEmitter(SseEmitter emitter) {
        this.emitters.add(emitter);

        try {
            emitter.send(SseEmitter.event().name("keep-alive").data(""));
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(emitter);
        }
    }

    @Override
    public void removeEmitter(SseEmitter emitter) {
        this.emitters.remove(emitter);
    }

    @Override
    public void sendNotification(String message) {
        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (IOException e) {
                // Handle disconnection
                emitter.completeWithError(e);
                deadEmitters.add(emitter);
            }
        });

        emitters.removeAll(deadEmitters);
    }
}
