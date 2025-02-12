package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.sns.InvalidGrantException;
import com.Alchive.backend.config.error.exception.sns.NoSuchDiscordUserException;
import com.Alchive.backend.config.error.exception.sns.NoSuchSnsIdException;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.sns.SnsCategory;
import com.Alchive.backend.repository.BoardRepository;
import com.Alchive.backend.repository.SnsReporitory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class DiscordService {
    private final JDA jda;
    private final BoardRepository boardRepository;
    private final SnsReporitory snsReporitory;

    @Value("${DISCORD_CLIENT_ID}")
    private String clientId;

    @Value("${DISCORD_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${DISCORD_REDIRECT_URI}")
    private String redirectUri;

    @Value("${DISCORD_BOT_TOKEN}")
    private String discordBotToken;
    private RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken(String code) {
        String getTokenUrl = "https://discord.com/api/oauth2/token";

        HttpHeaders getTokenHeaders = new HttpHeaders();
        getTokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> getTokenParams = new LinkedMultiValueMap<>();
        getTokenParams.add("client_id", clientId);
        getTokenParams.add("client_secret", clientSecret);
        getTokenParams.add("code", code);
        getTokenParams.add("grant_type", "authorization_code");
        getTokenParams.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getTokenParams, getTokenHeaders);
        ResponseEntity<Map> response = restTemplate.postForEntity(getTokenUrl, request, Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (responseBody.containsKey("error") && responseBody.get("error") == "invalid_grant" ) {
            throw new InvalidGrantException();
        }
        String accessToken = (String) responseBody.get("access_token");
        return accessToken;
    }

    public String getDiscordUserIdFromAccessToken(String accessToken) {
        String getUserInfoUrl = "https://discord.com/api/v10/users/@me";
        HttpHeaders accessTokenHeaders = new HttpHeaders();
        accessTokenHeaders.setBearerAuth(accessToken);

        HttpEntity<String> authRequest = new HttpEntity<>(accessTokenHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(getUserInfoUrl, HttpMethod.GET, authRequest, Map.class);
        Map<String, Object> userInfo = userInfoResponse.getBody();

        String discordUserId = (String) userInfo.get("id");
        return discordUserId;
    }

    public Sns getDiscordInfo(com.Alchive.backend.domain.user.User user) {
        Long userId = user.getId();
        Sns discordInfo = snsReporitory.findByUser_IdAndCategory(userId, SnsCategory.DISCORD)
                .orElseThrow(NoSuchSnsIdException::new);
        return discordInfo;
    }

    public void sendDm(String channelId, String message) {
        String sendMessageUrl = "https://discord.com/api/v10/channels/" + channelId + "/messages";
        HttpHeaders sendDmHeaders = new HttpHeaders();
        sendDmHeaders.set("Authorization", "Bot " + discordBotToken);
        sendDmHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> sendDmParams = new HashMap<>();
        sendDmParams.put("content", message);

        HttpEntity<Map<String, String>> sendMessageRequest = new HttpEntity<>(sendDmParams, sendDmHeaders);
        restTemplate.postForEntity(sendMessageUrl, sendMessageRequest, Map.class);
    }

    public String getDmChannel(String discordUserId) {
        String getDMChannelUrl = "https://discord.com/api/v10/users/@me/channels";
        Map<String, String> getDmParams = new HashMap<>();
        getDmParams.put("recipient_id", discordUserId);

        HttpHeaders botTokenHeader = new HttpHeaders();
        botTokenHeader.setContentType(MediaType.APPLICATION_JSON);
        botTokenHeader.set("Authorization", "Bot " + discordBotToken);

        HttpEntity<Map<String, String>> createDmRequest = new HttpEntity<>(getDmParams, botTokenHeader);
        ResponseEntity<Map> dmResponse = restTemplate.postForEntity(getDMChannelUrl, createDmRequest, Map.class);
        Map<String, Object> dmResponseBody = dmResponse.getBody();

        String channelId = (String) dmResponseBody.get("id");
        return channelId;
    }

    // JDA 사용 메서드
    public void sendDmJda(String discordUserId, String message) {
        User user = jda.retrieveUserById(discordUserId).complete();
        if (user != null) {
            user.openPrivateChannel().queue(channel ->
                    channel.sendMessage(message).queue()
            );
        } else {
            throw new NoSuchDiscordUserException();
        }
    }

//    @Scheduled(cron = "0 */1 * * * *") // todo: Quartz로 동적 스케줄링 작성하기
    public void sendMessageReminderBoard(com.Alchive.backend.domain.user.User user) {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(1);

        Board unSolvedBoard = boardRepository.findUnsolvedBoardAddedBefore(threeDaysAgo, user.getId());
        if (unSolvedBoard != null) {
            String message = String.format(":star-struck: %d일 전 도전했던 %d. %s 문제를 아직 풀지 못했어요. \n \n다시 도전해보세요! :facepunch: \n \n<%s|:link: 문제 풀러가기>",
                    ChronoUnit.DAYS.between(unSolvedBoard.getCreatedAt(), LocalDateTime.now()),
                    unSolvedBoard.getProblem().getNumber(),
                    unSolvedBoard.getProblem().getTitle(),
                    unSolvedBoard.getProblem().getUrl());

            Sns discordInfo = getDiscordInfo(user);
            sendDmJda(discordInfo.getSns_id(), message);
        } else {
            log.info("풀지 못한 문제가 존재하지 않습니다. ");
        }
    }
}
