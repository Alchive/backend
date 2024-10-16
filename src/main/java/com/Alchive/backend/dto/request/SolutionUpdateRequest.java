package com.Alchive.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SolutionUpdateRequest {
    private String description;
    @JsonCreator
    public SolutionUpdateRequest(@JsonProperty("description") String description) {
        this.description = description;
    }
}
