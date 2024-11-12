package com.Alchive.backend.sns;

import com.Alchive.backend.config.error.exception.sns.NoSuchSnsIdException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.sns.SnsCategory;
import com.Alchive.backend.dto.request.SnsCreateRequest;
import com.Alchive.backend.repository.SnsReporitory;
import com.Alchive.backend.service.SnsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.SLACK_DM_SEND_SUCCESS;

@Slf4j
@Tag(name = "슬랙", description = "슬랙 관련 API입니다. ")
@RestController
@RequestMapping("/api/v1/slack")
@RequiredArgsConstructor
public class SlackController {
    private final SlackService slackService;
    private final SnsService snsService;
    private final SnsReporitory snsReporitory;
    private final TokenService tokenService;

    @Operation(summary = "슬랙 봇 연결", description = "슬랙 액세스 토큰을 요청하고 DM 채널을 연결하는 api입니다. ")
    @GetMapping("/dm/open")
    public ResponseEntity<ResultResponse> openSlackDm(HttpServletRequest tokenRequest, @RequestParam String code) {
        // Bot Access Token, User Access Token, Slack User Id 요청
         SnsCreateRequest snsCreateRequest = slackService.getSlackInfo(code);
         log.info("사용자 slack 정보를 불러왔습니다. ");

        // Slack SNS 정보 저장
        snsService.createSns(tokenRequest, snsCreateRequest);
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
    public ResponseEntity<ResultResponse> sendSlackDm(HttpServletRequest tokenRequest, @RequestParam String message) {
        Long userId = tokenService.validateAccessToken(tokenRequest);
        Sns sns = snsReporitory.findByUser_IdAndCategory(userId, SnsCategory.SLACK)
                .orElseThrow(NoSuchSnsIdException::new);
        slackService.sendDm(sns.getSns_id(), sns.getBot_token(), message);
        return ResponseEntity.ok(ResultResponse.of(SLACK_DM_SEND_SUCCESS));
    }

//    @Operation(summary = "슬랙 봇 설정", description = "슬랙 봇 추가 시 메시지를 전송하는 메서드입니다. ")
//    @GetMapping("/added")
//    public void addedSlackBot() {
//        slackService.sendMessage(":wave: Hi from a bot written in Alchive!");
//        log.info("Slack Test");
//    }
//
//    @Operation(summary = "문제 리마인더", description = "30분마다 해결하지 못한 문제를 리마인드해주는 메서드입니다. ")
//    @GetMapping("/reminder")
//    public void sendReminder(HttpServletRequest tokenRequest) {
//        slackService.sendMessageReminderBoard(tokenRequest);
//    }
}