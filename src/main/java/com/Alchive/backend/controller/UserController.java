package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.UserCreateRequest;
import com.Alchive.backend.dto.request.UserUpdateRequest;
import com.Alchive.backend.dto.response.UserDetailResponseDTO;
import com.Alchive.backend.dto.response.UserResponseDTO;
import com.Alchive.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.*;


@Tag(name = "사용자", description = "사용자 관련 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/users") // 공통 url
public class UserController {
    private final UserService userService;

    @Operation(summary = "사용자 생성 메서드", description = "user를 생성하는 메서드입니다.")
    @PostMapping
    public ResponseEntity<ResultResponse> createUser(@RequestBody UserCreateRequest createRequest) {
        UserResponseDTO user = userService.createUser(createRequest);
        return ResponseEntity.ok(ResultResponse.of(USER_CREATE_SUCCESS, user));
    }

    @Operation(summary = "username 중복 확인 메서드", description = "username 중복을 검사하는 메서드입니다.")
    @GetMapping("/username/{name}")
    public ResponseEntity<ResultResponse> isDuplicateUsername(@PathVariable String name) {
        if (userService.isDuplicateUsername(name)) {
            return ResponseEntity.ok(ResultResponse.of(USER_USERNAME_DUPLICATED, true));
        }
        return ResponseEntity.ok(ResultResponse.of(USER_USERNAME_NOT_DUPLICATED, false));
    }

    @Operation(summary = "프로필 조회 메서드", description = "특정 사용자의 프로필 정보를 조회하는 메서드입니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<ResultResponse> getUserDetail(@PathVariable Long userId) {
        User user = userService.getUserDetail(userId);
        return ResponseEntity.ok(ResultResponse.of(USER_DETAIL_INFO_SUCCESS, new UserDetailResponseDTO(user)));
    }

    @Operation(summary = "프로필 수정 메서드", description = "특정 사용자의 프로필 정보를 수정하는 메서드입니다.")
    @PutMapping
    public ResponseEntity<ResultResponse> updateUser(@AuthenticationPrincipal User user, @RequestBody UserUpdateRequest updateRequest) {
        User newUser = userService.updateUserDetail(user, updateRequest);
        return ResponseEntity.ok(ResultResponse.of(USER_UPDATE_SUCCESS, new UserDetailResponseDTO(newUser)));
    }

    @Operation(summary = "사용자 삭제 메서드", description = "특정 사용자를 삭제하는 메서드입니다.")
    @DeleteMapping
    public ResponseEntity<ResultResponse> deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteUserDetail(user);
        return ResponseEntity.ok(ResultResponse.of(USER_DELETE_SUCCESS));
    }

}
