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
    private String token;
    private String channel;
    private String time;

    public SnsResponseDTO(Sns sns) {
        this.id = sns.getId();
        this.createdAt = sns.getCreatedAt();
        this.updatedAt = sns.getUpdatedAt();
        this.category = sns.getCategory();
        this.token = sns.getToken();
        this.channel = sns.getChannel();
        this.time = sns.getTime();
    }
}
