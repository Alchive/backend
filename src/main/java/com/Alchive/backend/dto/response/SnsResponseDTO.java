package com.Alchive.backend.dto.response;

import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.sns.SnsCategory;
import com.Alchive.backend.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class SnsResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SnsCategory category;
    private String bot_token;
    private String user_token;
    private String channel_id;
    private String sns_id;
    private String time;

    public SnsResponseDTO(Sns sns) {
        this.id = sns.getId();
        this.createdAt = sns.getCreatedAt();
        this.updatedAt = sns.getUpdatedAt();
        this.category = sns.getCategory();
        this.bot_token = sns.getBot_token();
        this.user_token = sns.getUser_token();
        this.channel_id = sns.getChannel_id();
        this.sns_id = sns.getSns_id();
        this.time = sns.getTime();
    }
}
