package com.Alchive.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "유저 이메일은 필수입니다. ")
    private String email;
    @NotBlank(message = "유저 이름은 필수입니다. ")
    private String name;
}