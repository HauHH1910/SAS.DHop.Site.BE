package com.sas.dhop.site.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/performance")
@Tag(name = "[Performance Controller]")
@Slf4j(topic = "[Performance Controller]")
public class PerformanceController {}
