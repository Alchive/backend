package com.Alchive.backend.slack;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "슬랙", description = "슬랙 관련 API입니다. ")
@RestController
@RequestMapping("/api/v1/slack")
@RequiredArgsConstructor
public class SlackController {
    @Autowired
    private final SlackService slackService;

    @Operation(summary = "슬랙 봇 설정", description = "슬랙 봇 추가 시 메시지를 전송하는 메서드입니다. ")
    @GetMapping("/added")
    public void addedSlackBot() {
        slackService.sendMessage(":wave: Hi from a bot written in Alchive!");
        log.info("Slack Test");
    }
}