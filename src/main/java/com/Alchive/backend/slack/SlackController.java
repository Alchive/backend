package com.Alchive.backend.slack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackController {
    @Autowired
    private final SlackService slackService;

    @GetMapping("/test")
    public void test() {
        slackService.sendMessage(":wave: Hi from a bot written in Alchive!");
        log.info("Slack Test");
    }
}