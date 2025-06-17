package com.sas.dhop.site.controller;

import com.sas.dhop.site.service.ServerSentEventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
@Tag(name = "[Server Sent Event Controller]")
@Slf4j(topic = "[Server Sent Event Controller]")
public class ServerSentEventController {

    private final ServerSentEventService serverSentEventService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sseEmitter() {
        return serverSentEventService.subscribe();
    }

}
