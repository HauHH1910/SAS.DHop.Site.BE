package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.service.ServerSentEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Servet Sent Event Service]")
public class ServerSentEventServiceImpl implements ServerSentEventService {
    @Override
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Or set a reasonable timeout

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                notificationService.addEmitter(emitter);
            } catch (Exception e) {
                emitter.completeWithError(e);
            } finally {
                executor.shutdown();
            }
        });

        emitter.onCompletion(() -> notificationService.removeEmitter(emitter));
        emitter.onTimeout(() -> {
            notificationService.removeEmitter(emitter);
            emitter.complete();
        });
        emitter.onError((e) -> notificationService.removeEmitter(emitter));

        return emitter;
    }
}
