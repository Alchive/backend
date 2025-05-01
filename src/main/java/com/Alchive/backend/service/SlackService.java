package com.Alchive.backend.service;

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
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class SlackService {
    private final static String SLACK_TOKEN_REQUEST_URL = "https://slack.com/api/oauth.v2.access";
    private final static String SLACK_SEND_DM_REQUEST_URL = "https://slack.com/api/chat.postMessage";
    @Value("${SLACK_CLIENT_ID}")
    private String clientId;

    @Value("${SLACK_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${SLACK_REDIRECT_URI}")
    private String redirectUri;

    @Value("${SLACK_BOT_TOKEN}")
    private String slackBotToken;

    private final BoardRepository boardRepository;
    private final SnsReporitory snsReporitory;
    private final SnsService snsService;

    public void initializeSlackChannelAndSaveSnsInfo(User user, String code) {
        ResponseEntity<Map> response = getSlackAuthUserInfo(code);
        Map<String, Object> authedUser = (Map<String, Object>) snsService.parseResponse(response, "authed_user");
        String slackUserId = (String) authedUser.get("id");
        String userAccessToken = (String) authedUser.get("access_token");
        String botAccessToken = (String) snsService.parseResponse(response, "access_token");

        SnsCreateRequest snsCreateRequest = SnsCreateRequest.builder()
                .category(SnsCategory.SLACK)
                .sns_id(slackUserId)
                .bot_token(botAccessToken)
                .user_token(userAccessToken)
                .time("0 0 18 ? * MON")
                .build();
        Sns slackSns = Sns.of(user, snsCreateRequest);
        snsService.createSns(slackSns);
    }

    private ResponseEntity<Map> getSlackAuthUserInfo (String code) {
        HttpHeaders getTokenHeaders = new HttpHeaders();
        getTokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> getTokenParams = new LinkedMultiValueMap<>();
        getTokenParams.add("client_id", clientId);
        getTokenParams.add("client_secret", clientSecret);
        getTokenParams.add("redirect_uri", redirectUri);
        getTokenParams.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getTokenParams, getTokenHeaders);
        ResponseEntity<Map> response = snsService.sendRestTemplateExchange(SLACK_TOKEN_REQUEST_URL, HttpMethod.POST, request);
        return response;
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

            sendSlackDm(user, message);
        } else {
            log.info("풀지 못한 문제가 존재하지 않습니다. ");
        }
    }

    public void sendSlackDm(User user, String message) {
        Sns slackInfo = getSlackInfo(user);
        String slackUserId = slackInfo.getSns_id();
        String botAccessToken = slackInfo.getBot_token();
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.setContentType(MediaType.APPLICATION_JSON);
        httpHeader.setBearerAuth(botAccessToken);

        Map<String, String> httpBody = new HashMap<>();
        httpBody.put("channel", slackUserId);
        httpBody.put("text", message);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(httpBody, httpHeader);
        snsService.sendRestTemplateExchange(SLACK_SEND_DM_REQUEST_URL, HttpMethod.POST, request);
    }

    private Sns getSlackInfo(User user) {
        Long userId = user.getId();
        Sns slackInfo = snsReporitory.findByUser_IdAndCategory(userId, SnsCategory.SLACK)
                .orElseThrow(NoSuchSnsIdException::new);
        return slackInfo;
    }
}
