package com.Alchive.backend.controller;

import com.Alchive.backend.domain.User;
import com.Alchive.backend.dto.response.UserViewResponse;
import com.Alchive.backend.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final UserDetailService userDetailService;

    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<UserViewResponse> findUser(@PathVariable Long userId) {
        User user = userDetailService.getUserDetail(userId);
        return ResponseEntity.ok()
                .body(new UserViewResponse(user));
    }
}
