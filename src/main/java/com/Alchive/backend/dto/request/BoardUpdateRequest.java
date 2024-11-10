package com.Alchive.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardUpdateRequest {
    private final String description;

    @JsonCreator
    public BoardUpdateRequest(@JsonProperty("description") String description) {
        this.description = description;
    }
}
