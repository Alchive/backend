package com.Alchive.backend.sns;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.domain.sns.SnsCategory;
import com.Alchive.backend.dto.request.SnsCreateRequest;
import com.Alchive.backend.service.SnsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.DISCORD_DM_SEND_SUCCESS;

@Tag(name = "디스코드", description = "디스코드 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/discord")
public class DiscordController {
    private final SnsService snsService;
    private final DiscordService discordService;

    @Operation(summary = "디스코드 봇 연결", description = "디스코드 액세스 토큰을 요청하고 DM 채널을 연결하는 api입니다. ")
    @GetMapping("/dm/open")
    public ResponseEntity<ResultResponse> openDiscordDm(HttpServletRequest tokenRequest, @RequestParam String code) {
        // Access Token 요청
        String accessToken = discordService.getAccessToken(code);
        log.info("Access Token 반환 완료: " + accessToken);

        // 사용자 DISCORD USER ID 요청
        String discordUserId = discordService.getDiscordUserIdFromAccessToken(accessToken);
        log.info("Discord User Id 반환 완료: " + discordUserId);

        // DM 채널 생성 요청
        String channelId = discordService.getDmChannel(discordUserId);
        log.info("DM 채널 생성 완료");

        // Discord SNS 정보 저장
        SnsCreateRequest snsCreateRequest = SnsCreateRequest.builder()
                .category(SnsCategory.DISCORD)
                .sns_id(discordUserId) // Discord User Id
                .channel_id(channelId) // Discord Channel Id
                .time("0 0 18 ? * MON")
                .build();
        snsService.createSns(tokenRequest, snsCreateRequest);
        log.info("SNS 정보 저장 완료");

        // DM 전송 요청
        discordService.sendDm(channelId, "안녕하세요! 이제부터 풀지 못한 문제들을 정해진 시간에 알려드릴게요. ");

        return ResponseEntity.ok(ResultResponse.of(DISCORD_DM_SEND_SUCCESS));
    }

    @Operation(summary = "디스코드 DM 전송", description = "디스코드 DM으로 메시지를 전송하는 api입니다. ")
    @PostMapping("dm/send")
    public ResponseEntity<ResultResponse> sendDiscordDm(HttpServletRequest tokenRequest, @RequestParam String message) {
        String discordUserId = discordService.getDiscordUserId(tokenRequest);
        discordService.sendDmJda(discordUserId, message);
        return ResponseEntity.ok(ResultResponse.of(DISCORD_DM_SEND_SUCCESS));
    }
}
