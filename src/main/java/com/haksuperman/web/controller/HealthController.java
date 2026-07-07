package com.haksuperman.web.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthController {

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String health() {
        // 고정 JSON 하나에 직렬화 라이브러리는 과하다 (ADR 철학: 최소 구현)
        return "{\"status\":\"UP\"}";
    }
}
