package com.Alchive.backend.dto.request;

import com.Alchive.backend.domain.sns.SnsCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SnsCreateRequest {
    @NotNull(message = "소셜 카테고리는 필수입니다. ")
    private SnsCategory category;
    private String bot_token;
    private String user_token;
    private String channel_id;
    private String sns_id;
    private String time;
}
