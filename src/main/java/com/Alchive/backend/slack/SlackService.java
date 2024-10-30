package com.Alchive.backend.slack;

import com.Alchive.backend.domain.board.BoardStatus;
import com.Alchive.backend.dto.request.BoardCreateRequest;
import com.Alchive.backend.dto.response.BoardResponseDTO;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SlackService {
    @Value("${SLACK_TOKEN}")
    private String token;

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
        if (boardCreateRequest.getStatus() != BoardStatus.NOT_SUBMITTED) {
            message = String.format(":partying_face: %d. %s 문제를 해결했습니다! \n \n<http://localhost:5173/detail/%s|:link: alchive.com/detail/%s>",
                    boardCreateRequest.getProblemCreateRequest().getNumber(),
                    boardCreateRequest.getProblemCreateRequest().getTitle(),
                    board.getId(), board.getId());
        } else {
            message = String.format(":round_pushpin: %d. %s 문제를 저장했습니다. \n \n3일 뒤 리마인드 알림을 보내드릴게요! :saluting_face: \n \n<%s|:link: 문제 보러가기>",
                    boardCreateRequest.getProblemCreateRequest().getNumber(),
                    boardCreateRequest.getProblemCreateRequest().getTitle(),
                    boardCreateRequest.getProblemCreateRequest().getUrl());
        }
        sendMessage(message);
    }
}
