package com.Alchive.backend.controller;

import com.Alchive.backend.domain.User;
import com.Alchive.backend.dto.request.UserUpdateRequest;
import com.Alchive.backend.dto.response.ApiResponse;
import com.Alchive.backend.dto.response.UserDetailResponseDTO;
import com.Alchive.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자", description = "사용자 관련 api입니다. ")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users") // 공통 url
public class UserController {
    private final UserService userService;

    @Operation(summary = "프로필 조회 메서드", description = "특정 사용자의 프로필 정보를 조회하는 메서드입니다. ")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> findUser(@PathVariable Long userId) {
        User user = userService.getUserDetail(userId);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "유저 정보를 불러왔습니다.", new UserDetailResponseDTO(user)));
    }

    @Operation(summary = "프로필 수정 메서드", description = "특정 사용자의 프로필 정보를 수정하는 메서드입니다. ")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        userService.updateUserDetail(userId, request);
        return ResponseEntity.ok()
                .body(new ApiResponse(HttpStatus.OK.value(), "프로필 수정이 완료되었습니다."));
    }
}
