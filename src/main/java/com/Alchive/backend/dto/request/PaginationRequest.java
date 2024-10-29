package com.Alchive.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PaginationRequest {
    private int offset = 0;
    private int limit = 20;
}
