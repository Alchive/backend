package com.Alchive.backend.controller;

import com.Alchive.backend.domain.User;
import com.Alchive.backend.dto.request.UserUpdateRequest;
import com.Alchive.backend.dto.response.ApiResponse;
import com.Alchive.backend.dto.response.UserDetailData;
import com.Alchive.backend.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserDetailService userDetailService;

    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<ApiResponse> findUser(@PathVariable Long userId) {
        User user = userDetailService.getUserDetail(userId);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "유저 정보를 불러왔습니다. ", new UserDetailData(user)));
    }

    @PutMapping("/api/v1/users/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        userDetailService.updateUserDetail(userId, request);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "프로필 수정이 완료되었습니다. ", null));
    }
}
