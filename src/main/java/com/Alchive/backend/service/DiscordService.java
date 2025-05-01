package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.sns.InvalidGrantException;
import com.Alchive.backend.config.error.exception.sns.NoSuchDiscordUserException;
import com.Alchive.backend.config.error.exception.sns.NoSuchSnsIdException;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.sns.SnsCategory;
import com.Alchive.backend.dto.request.SnsCreateRequest;
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
public class DiscordService{
    private final static String DISCORD_USER_INFO_REQUEST_URL = "https://discord.com/api/v10/users/@me";
    private final static String DISCORD_ACCESS_TOKEN_REQUEST_URL = "https://discord.com/api/oauth2/token";
    private final static String DISCORD_DM_CHANNEL_REQUEST_URL = "https://discord.com/api/v10/users/@me/channels";
    private final JDA jda;
    private final BoardRepository boardRepository;
    private final SnsReporitory snsReporitory;
    private final SnsService snsService;
    @Value("${DISCORD_CLIENT_ID}")
    private String clientId;

    @Value("${DISCORD_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${DISCORD_REDIRECT_URI}")
    private String redirectUri;

    @Value("${DISCORD_BOT_TOKEN}")
    private String discordBotToken;
    private RestTemplate restTemplate = new RestTemplate();

    public void initializeDiscordChannleAndSaveSnsInfo(com.Alchive.backend.domain.user.User user, String code) {
        String accessToken = getAccessToken(code);
        String discordUserId = getDiscordUserIdFromAccessToken(accessToken);
        String channelId = getDmChannel(discordUserId);

        SnsCreateRequest snsCreateRequest = SnsCreateRequest.builder()
                .category(SnsCategory.DISCORD)
                .sns_id(discordUserId) // Discord User Id
                .channel_id(channelId) // Discord Channel Id
                .time("0 0 18 ? * MON")
                .build();
        Sns discordInfo = Sns.of(user, snsCreateRequest);
        saveDiscordInfo(discordInfo);
    }

    private String getAccessToken(String code) {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("client_id", clientId);
        httpBody.add("client_secret", clientSecret);
        httpBody.add("grant_type", "authorization_code");
        httpBody.add("redirect_uri", redirectUri);
        httpBody.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpBody, httpHeader);
        ResponseEntity<Map> response = sendRestTemplateExchange(DISCORD_ACCESS_TOKEN_REQUEST_URL, HttpMethod.POST, request);
        String accessToken = parseResponse(response, "access_token");
        return accessToken;
    }

    private ResponseEntity<Map> sendRestTemplateExchange(String requestUrl, HttpMethod method, HttpEntity request) {
        ResponseEntity<Map> response = restTemplate.exchange(requestUrl, method, request, Map.class);
        return response;
    }

    private String parseResponse(ResponseEntity<Map> response, String targetParam) {
        Map<String, Object> responseBody = response.getBody();
        checkInvalidGrant(responseBody);
        String targetResult = (String) responseBody.get(targetParam);
        return targetResult;
    }

    private void checkInvalidGrant(Map<String, Object> responseBody) {
        if (responseBody.containsKey("error") && responseBody.get("error") == "invalid_grant" ) {
            throw new InvalidGrantException();
        }
    }

    private String getDiscordUserIdFromAccessToken(String accessToken) {
        HttpHeaders accessTokenHeaders = new HttpHeaders();
        accessTokenHeaders.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(accessTokenHeaders);
        ResponseEntity<Map> response = sendRestTemplateExchange(DISCORD_USER_INFO_REQUEST_URL, HttpMethod.GET, request);
        String discordUserId = parseResponse(response, "id");
        return discordUserId;
    }

    private String getDmChannel(String discordUserId) {
        Map<String, String> httpBody = new HashMap<>();
        httpBody.put("recipient_id", discordUserId);
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setContentType(MediaType.APPLICATION_JSON);
        httpHeader.set("Authorization", "Bot " + discordBotToken);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(httpBody, httpHeader);

        ResponseEntity<Map> response = sendRestTemplateExchange(DISCORD_DM_CHANNEL_REQUEST_URL, HttpMethod.POST, request);
        String channelId = parseResponse(response, "id");
        return channelId;
    }

    private void saveDiscordInfo(Sns discordInfo) {
        snsService.createSns(discordInfo);
    }

    //    @Scheduled(cron = "0 */1 * * * *") // todo: Quartz로 동적 스케줄링 작성하기
    private void sendMessageReminderBoard(com.Alchive.backend.domain.user.User user) {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(1);

        Board unSolvedBoard = boardRepository.findUnsolvedBoardAddedBefore(threeDaysAgo, user.getId());
        if (unSolvedBoard != null) {
            String message = String.format(":star-struck: %d일 전 도전했던 %d. %s 문제를 아직 풀지 못했어요. \n \n다시 도전해보세요! :facepunch: \n \n<%s|:link: 문제 풀러가기>",
                    ChronoUnit.DAYS.between(unSolvedBoard.getCreatedAt(), LocalDateTime.now()),
                    unSolvedBoard.getProblem().getNumber(),
                    unSolvedBoard.getProblem().getTitle(),
                    unSolvedBoard.getProblem().getUrl());

            Sns discordInfo = getDiscordInfo(user);
            sendDmJda(user, message);
        } else {
            log.info("풀지 못한 문제가 존재하지 않습니다. ");
        }
    }

    public void sendDm(com.Alchive.backend.domain.user.User user, String message) {
        String channelId = getUserChannelId(user);
        String sendMessageUrl = "https://discord.com/api/v10/channels/" + channelId + "/messages";

        HttpHeaders sendDmHeaders = new HttpHeaders();
        sendDmHeaders.set("Authorization", "Bot " + discordBotToken);
        sendDmHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> sendDmParams = new HashMap<>();
        sendDmParams.put("content", message);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(sendDmParams, sendDmHeaders);

        sendRestTemplateExchange(sendMessageUrl, HttpMethod.POST, request);
    }

    private String getUserChannelId(com.Alchive.backend.domain.user.User user) {
        return getDiscordInfo(user).getChannel_id();
    }

    private Sns getDiscordInfo(com.Alchive.backend.domain.user.User user) {
        Long userId = user.getId();
        Sns discordInfo = snsReporitory.findByUser_IdAndCategory(userId, SnsCategory.DISCORD)
                .orElseThrow(NoSuchSnsIdException::new);
        return discordInfo;
    }

    // JDA 사용 메서드
    public void sendDmJda(com.Alchive.backend.domain.user.User user, String message) {
        String discordUserId = getDiscordInfo(user).getSns_id();
        User jdaUser = jda.retrieveUserById(discordUserId).complete();
        checkUserNull(jdaUser);
        jdaUser.openPrivateChannel().queue(channel ->
                channel.sendMessage(message).queue());
    }

    private void checkUserNull(User user) {
        if (user == null) {
            throw new NoSuchDiscordUserException();
        }
    }
}
