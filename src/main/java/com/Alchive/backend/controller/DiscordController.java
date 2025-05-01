package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.service.DiscordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.DISCORD_DM_SEND_SUCCESS;

@Tag(name = "디스코드", description = "디스코드 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v2/discord")
public class DiscordController {
    private final DiscordService discordService;

    @Operation(summary = "디스코드 봇 연결", description = "디스코드 액세스 토큰을 요청하고 DM 채널을 연결하는 api입니다. ")
    @GetMapping("/dm/open")
    public ResponseEntity<ResultResponse> initializeDiscordChannelAndSendWelcomeDM(@AuthenticationPrincipal User user, @RequestParam String code) {
        discordService.initializeDiscordChannelAndSaveSnsInfo(user, code);
        discordService.sendDiscordDm(user, "안녕하세요! 이제부터 풀지 못한 문제들을 정해진 시간에 알려드릴게요. ");
        return ResponseEntity.ok(ResultResponse.of(DISCORD_DM_SEND_SUCCESS));
    }

    @Operation(summary = "디스코드 DM 전송", description = "디스코드 DM으로 메시지를 전송하는 api입니다. ")
    @PostMapping("dm/send")
    public ResponseEntity<ResultResponse> sendDiscordDm(@AuthenticationPrincipal User user, @RequestParam String message) {
        discordService.sendJdaDm(user, message);
        return ResponseEntity.ok(ResultResponse.of(DISCORD_DM_SEND_SUCCESS));
    }
}
