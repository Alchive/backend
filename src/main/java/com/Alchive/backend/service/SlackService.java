package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.sns.InvalidGrantException;
import com.Alchive.backend.config.error.exception.sns.NoSuchSnsIdException;
import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.sns.SnsCategory;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.SnsCreateRequest;
import com.Alchive.backend.repository.BoardRepository;
import com.Alchive.backend.repository.SnsReporitory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class SlackService {
    @Value("${SLACK_CLIENT_ID}")
    private String clientId;

    @Value("${SLACK_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${SLACK_REDIRECT_URI}")
    private String redirectUri;

    @Value("${SLACK_BOT_TOKEN}")
    private String slackBotToken;

    private RestTemplate restTemplate = new RestTemplate();
    private BoardRepository boardRepository;
    private SnsReporitory snsReporitory;

    public SnsCreateRequest getSlackInfo(String code) {
        String getTokenUrl = "https://slack.com/api/oauth.v2.access";

        HttpHeaders getTokenHeaders = new HttpHeaders();
        getTokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> getTokenParams = new LinkedMultiValueMap<>();
        getTokenParams.add("client_id", clientId);
        getTokenParams.add("client_secret", clientSecret);
        getTokenParams.add("code", code);
        getTokenParams.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getTokenParams, getTokenHeaders);
        ResponseEntity<Map> reponse = restTemplate.postForEntity(getTokenUrl, request, Map.class);
        Map<String, Object> responseBody = reponse.getBody();

        if (responseBody.get("ok") == "false") {
            throw new InvalidGrantException();
        }

        Map<String, Object> authedUser = (Map<String, Object>) responseBody.get("authed_user");
        String slackUserId = (String) authedUser.get("id");
        String botAccessToken = (String) responseBody.get("access_token");
        String userAccessToken = (String) authedUser.get("access_token");

        SnsCreateRequest snsCreateRequest = SnsCreateRequest.builder()
                .category(SnsCategory.SLACK)
                .sns_id(slackUserId)
                .bot_token(botAccessToken)
                .user_token(userAccessToken)
                .time("0 0 18 ? * MON")
                .build();

        return snsCreateRequest;
    }

    public Sns getSlackInfo(User user) {
        Long userId = user.getId();
        Sns slackInfo = snsReporitory.findByUser_IdAndCategory(userId, SnsCategory.SLACK)
                .orElseThrow(NoSuchSnsIdException::new);
        return slackInfo;
    }

    public void sendDm(String slackUserId, String botAccessToken, String message) {
        String sendDmUrl = "https://slack.com/api/chat.postMessage";
        HttpHeaders sendDmHeaders = new HttpHeaders();
        sendDmHeaders.setContentType(MediaType.APPLICATION_JSON);
        sendDmHeaders.setBearerAuth(botAccessToken);

        Map<String, String> sendDmParams = new HashMap<>();
        sendDmParams.put("channel", slackUserId);
        sendDmParams.put("text", message);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(sendDmParams, sendDmHeaders);
        restTemplate.postForEntity(sendDmUrl, request, Map.class);
    }

    //    @Scheduled(cron = "0 0 * * * *") // todo: Quartz로 동적 스케줄링 작성하기
    public void sendMessageReminderBoard(User user) {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(1);

        Board unSolvedBoard = boardRepository.findUnsolvedBoardAddedBefore(threeDaysAgo, user.getId());
        if (unSolvedBoard != null) {
            String message = String.format(":star-struck: %d일 전 도전했던 %d. %s 문제를 아직 풀지 못했어요. \n \n다시 도전해보세요! :facepunch: \n \n<%s|:link: 문제 풀러가기>",
                    ChronoUnit.DAYS.between(unSolvedBoard.getCreatedAt(), LocalDateTime.now()),
                    unSolvedBoard.getProblem().getNumber(),
                    unSolvedBoard.getProblem().getTitle(),
                    unSolvedBoard.getProblem().getUrl());

            Sns slackInfo = getSlackInfo(user);
            sendDm(slackInfo.getSns_id(), slackInfo.getBot_token(), message);
        } else {
            log.info("풀지 못한 문제가 존재하지 않습니다. ");
        }
    }
}
