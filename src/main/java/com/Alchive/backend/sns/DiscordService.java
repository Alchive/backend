package com.Alchive.backend.sns;

import com.Alchive.backend.config.error.exception.sns.InvalidGrantException;
import com.Alchive.backend.config.error.exception.sns.NoSuchSnsIdException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.sns.SnsCategory;
import com.Alchive.backend.repository.BoardRepository;
import com.Alchive.backend.repository.SnsReporitory;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private JDA jda;
    private final BoardRepository boardRepository;
    private final SnsReporitory snsReporitory;
    private final TokenService tokenService;

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

    public String getDiscordUserId(HttpServletRequest tokenRequest) {
        Long userId = tokenService.validateAccessToken(tokenRequest);
        Sns snsInfo = snsReporitory.findByUser_IdAndCategory(userId, SnsCategory.DISCORD)
                .orElseThrow(NoSuchSnsIdException::new);
        String discordUserId = snsInfo.getToken();
        return discordUserId;
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

//    @Scheduled(cron = "0 */1 * * * *")
    public void sendMessageReminderBoard(HttpServletRequest tokenRequest) {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(1);

        Board unSolvedBoard = boardRepository.findUnsolvedBoardAddedBefore(threeDaysAgo);

        if (unSolvedBoard != null) {
            String message = String.format(":star-struck: %d일 전 도전했던 %d. %s 문제를 아직 풀지 못했어요. \n \n다시 도전해보세요! :facepunch: \n \n<%s|:link: 문제 풀러가기>",
                    ChronoUnit.DAYS.between(unSolvedBoard.getCreatedAt(), LocalDateTime.now()),
                    unSolvedBoard.getProblem().getNumber(),
                    unSolvedBoard.getProblem().getTitle(),
                    unSolvedBoard.getProblem().getUrl());
            String discordUserId = getDiscordUserId(tokenRequest);
            String channelId = getDmChannel(discordUserId);
            sendDm(channelId,message);
        } else {
            log.info("풀지 못한 문제가 존재하지 않습니다. ");
        }
    }
}
