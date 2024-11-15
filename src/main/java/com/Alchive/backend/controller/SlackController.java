package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.SnsCreateRequest;
import com.Alchive.backend.service.SnsService;
import com.Alchive.backend.service.SlackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.SLACK_DM_SEND_SUCCESS;

@Slf4j
@Tag(name = "슬랙", description = "슬랙 관련 API입니다. ")
@RestController
@RequestMapping("/api/v2/slack")
@RequiredArgsConstructor
public class SlackController {
    private final SlackService slackService;
    private final SnsService snsService;

    @Operation(summary = "슬랙 봇 연결", description = "슬랙 액세스 토큰을 요청하고 DM 채널을 연결하는 api입니다. ")
    @GetMapping("/dm/open")
    public ResponseEntity<ResultResponse> openSlackDm(@AuthenticationPrincipal User user, @RequestParam String code) {
        // Bot Access Token, User Access Token, Slack User Id 요청
         SnsCreateRequest snsCreateRequest = slackService.getSlackInfo(code);
         log.info("사용자 slack 정보를 불러왔습니다. ");

        // Slack SNS 정보 저장
        snsService.createSns(user, snsCreateRequest);
        log.info("사용자 slack 정보를 저장했습니다. ");

        // DM 전송 요청
        String slackUserId = snsCreateRequest.getSns_id();
        String botAccessToken = snsCreateRequest.getBot_token();

        slackService.sendDm(slackUserId, botAccessToken, "안녕하세요! 이제부터 풀지 못한 문제들을 정해진 시간에 알려드릴게요. ");
        log.info("봇을 사용자 slack 채널에 추가했습니다. ");
        return ResponseEntity.ok(ResultResponse.of(SLACK_DM_SEND_SUCCESS));
    }

    @Operation(summary = "슬랙 DM 전송", description = "슬랙 DM으로 메시지를 전송하는 api입니다. ")
    @PostMapping("dm/send")
    public ResponseEntity<ResultResponse> sendSlackDm(@AuthenticationPrincipal User user, @RequestParam String message) {
        Sns sns = slackService.getSlackInfo(user);
        slackService.sendDm(sns.getSns_id(), sns.getBot_token(), message);
        return ResponseEntity.ok(ResultResponse.of(SLACK_DM_SEND_SUCCESS));
    }
}