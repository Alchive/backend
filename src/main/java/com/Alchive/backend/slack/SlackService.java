package com.Alchive.backend.slack;

import com.Alchive.backend.domain.board.Board;
import com.Alchive.backend.domain.board.BoardStatus;
import com.Alchive.backend.dto.request.BoardCreateRequest;
import com.Alchive.backend.dto.response.BoardResponseDTO;
import com.Alchive.backend.repository.BoardRepository;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class SlackService {
    @Value("${SLACK_TOKEN}")
    private String token;

    private final BoardRepository boardRepository;

    private String channel = "#alchive-bot";

    public void sendMessage(String message) {
        try {
            MethodsClient methods = Slack.getInstance().methods(token);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(message)
                    .build();

            methods.chatPostMessage(request);
            log.info("Slack - Message 전송 완료 : {}", message);
        } catch (Exception e) {
            log.warn("Slack Error - {}", e.getMessage());
        }
    }

    public void sendMessageCreateBoard(BoardCreateRequest boardCreateRequest, BoardResponseDTO board) {
        String message = "";
        if (boardCreateRequest.getStatus() == BoardStatus.CORRECT) {
            message = String.format(":partying_face: %d. %s 문제를 해결했습니다! \n \n<http://localhost:5173/detail/%s|:link: alchive.com/detail/%s>",
                    boardCreateRequest.getProblemCreateRequest().getNumber(),
                    boardCreateRequest.getProblemCreateRequest().getTitle(),
                    board.getId(), board.getId());
        } else if (boardCreateRequest.getStatus() == BoardStatus.NOT_SUBMITTED) {
            message = String.format(":round_pushpin: %d. %s 문제를 저장했습니다. \n \n리마인드 알림을 보내드릴게요! :saluting_face: \n \n<%s|:link: 문제 보러가기>",
                    boardCreateRequest.getProblemCreateRequest().getNumber(),
                    boardCreateRequest.getProblemCreateRequest().getTitle(),
                    boardCreateRequest.getProblemCreateRequest().getUrl());
        } else {
            return;
        }
        sendMessage(message);
    }

    @Scheduled(cron = "0 0 18 * * *") // 매일 오후 6시마다
    public void sendMessageReminderBoard() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        Board unSolvedBoard = boardRepository.findUnsolvedBoardAddedBefore(threeDaysAgo);

        if (unSolvedBoard != null) {
            String message = String.format(":star-struck: %d일 전 도전했던 %d. %s 문제를 아직 풀지 못했어요. \n \n다시 도전해보세요! :facepunch: \n \n<%s|:link: 문제 풀러가기>",
                    ChronoUnit.DAYS.between(unSolvedBoard.getCreatedAt(), LocalDateTime.now()),
                    unSolvedBoard.getProblem().getNumber(),
                    unSolvedBoard.getProblem().getTitle(),
                    unSolvedBoard.getProblem().getUrl());
            sendMessage(message);
        } else {
            log.info("풀지 못한 문제가 존재하지 않습니다. ");
        }
    }
}
