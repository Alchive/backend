package com.Alchive.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
public class BoardMemoUpdateRequest {
    private final String memo;
    @JsonCreator
    public BoardMemoUpdateRequest(@JsonProperty("memo") String memo) {
        this.memo = memo;
    }
}