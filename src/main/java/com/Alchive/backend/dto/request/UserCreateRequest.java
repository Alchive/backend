package com.Alchive.backend.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserCreateRequest {
    private String userEmail;
    private String userName;
}