package com.sas.dhop.site.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ServerSentEventService {

    SseEmitter subscribe();
}
