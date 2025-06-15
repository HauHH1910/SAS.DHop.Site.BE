package com.sas.dhop.site.service.impl;

import com.sas.dhop.site.service.ServerSentEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "[Servet Sent Event Service]")
public class ServerSentEventServiceImpl implements ServerSentEventService {
    @Override
    public SseEmitter subscribe() {
        return null;
    }
}
