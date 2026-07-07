package com.haksuperman.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private final String version;
    private final String buildTimestamp;

    public WelcomeController() {
        Properties props = new Properties();
        try (InputStream in = getClass().getResourceAsStream("/version.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            // 버전 정보가 없어도 앱은 동작해야 하므로 폴백으로 처리
        }
        this.version = props.getProperty("app.version", "unknown");
        this.buildTimestamp = props.getProperty("build.timestamp", "unknown");
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("msg", "Hello, CI/CD World!");
        model.addAttribute("today", LocalDate.now().toString());
        model.addAttribute("version", version);
        model.addAttribute("buildTimestamp", buildTimestamp);
        return "index";
    }
}
