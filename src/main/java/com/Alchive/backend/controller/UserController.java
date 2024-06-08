package com.Alchive.backend.controller;

import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.domain.User;
import com.Alchive.backend.dto.request.UserCreateRequest;
import com.Alchive.backend.dto.request.UserUpdateRequest;
import com.Alchive.backend.dto.response.UserDetailResponseDTO;
import com.Alchive.backend.dto.response.UserResponseDTO;
import com.Alchive.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.Alchive.backend.config.result.ResultCode.*;


@Tag(name = "사용자", description = "사용자 관련 api입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users") // 공통 url
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @Operation(summary = "사용자 생성 메서드", description = "user를 생성하는 메서드입니다.")
    @PostMapping
    public ResponseEntity<ResultResponse> createUser(@RequestBody UserCreateRequest createRequest) {
        UserResponseDTO newUser = userService.createUser(createRequest);
        return ResponseEntity.ok(ResultResponse.of(USER_CREATE_SUCCESS, newUser));
    }

    @Operation(summary = "username 중복 확인 메서드", description = "username 중복을 검사하는 메서드입니다.")
    @GetMapping("/username/{userName}")
    public ResponseEntity<ResultResponse> isDuplicateUsername(@PathVariable String userName) {
        if(userService.isDuplicateUsername(userName)) {
            return ResponseEntity.ok(ResultResponse.of(USER_USERNAME_DUPLICATED, true));
        }
        return ResponseEntity.ok(ResultResponse.of(USER_USERNAME_NOT_DUPLICATED, false));
    }

    @Operation(summary = "프로필 조회 메서드", description = "특정 사용자의 프로필 정보를 조회하는 메서드입니다.")
    @GetMapping
    public ResponseEntity<ResultResponse> findUser(HttpServletRequest request) {
        User user = userService.getUserDetail(request);
        return ResponseEntity.ok(ResultResponse.of(USER_DETAIL_INFO_SUCCESS, new UserDetailResponseDTO(user)));
    }

    @Operation(summary = "프로필 수정 메서드", description = "특정 사용자의 프로필 정보를 수정하는 메서드입니다.")
    @PutMapping
    public ResponseEntity<ResultResponse> updateUser(HttpServletRequest request, @RequestBody UserUpdateRequest updateRequest) {
        userService.updateUserDetail(request, updateRequest);
        return ResponseEntity.ok(ResultResponse.of(USER_UPDATE_SUCCESS));
    }

    @Operation(summary = "사용자 삭제 메서드", description = "특정 사용자를 삭제하는 메서드입니다.")
    @DeleteMapping
    public ResponseEntity<ResultResponse> deleteUser(HttpServletRequest request) {
        userService.deleteUserDetail(request);
        return ResponseEntity.ok(ResultResponse.of(USER_DELETE_SUCCESS));
    }

    @Operation(summary = "액세스 토큰 재발급 메서드", description = "리프레시 토큰으로 액세스 토큰을 재발급하는 메서드입니다.")
    @GetMapping("/auth/token")
    public ResponseEntity<ResultResponse> refreshAccessToken(HttpServletRequest request) {
        String accessToken = tokenService.refreshAccessToken(request);
        return ResponseEntity.ok(ResultResponse.of(TOKEN_ACCESS_SUCCESS, accessToken));
    }
}
