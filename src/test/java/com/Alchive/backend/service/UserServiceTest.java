package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.user.UserEmailExistException;
import com.Alchive.backend.config.error.exception.user.UserNameExistException;
import com.Alchive.backend.config.jwt.JwtTokenProvider;
import com.Alchive.backend.config.redis.RefreshTokenService;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.UserCreateRequest;
import com.Alchive.backend.dto.request.UserUpdateRequest;
import com.Alchive.backend.dto.response.UserResponseDTO;
import com.Alchive.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService sut;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RefreshTokenService refreshTokenService;

    String email = "test@email.com";
    String name = "testName";
    String accessToken = "testAccessToken";
    String refreshToken = "testRefreshToken";
    User user = new User(email, name);

    @DisplayName("UserService 사용자 생성 - 성공")
    @Test
    public void createUserSuccess() {
        UserCreateRequest request = new UserCreateRequest(email, name);
        when(userRepository.save(any(User.class))).thenReturn(new User(email, name));
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByName(name)).thenReturn(false);
        when(jwtTokenProvider.createAccessToken(email)).thenReturn(accessToken);
        when(refreshTokenService.createRefreshToken(email)).thenReturn(refreshToken);
        doNothing().when(refreshTokenService).saveRefreshToken(email, refreshToken);

        UserResponseDTO result = sut.createUser(request);

        Assertions.assertEquals(email, result.getUserEmail());
        Assertions.assertEquals(name, result.getUserName());
        Assertions.assertEquals(accessToken, result.getAccessToken());
    }

    @DisplayName("UserService 사용자 생성 - 이메일 중복 실패")
    @Test
    public void createUserFailByEmailDuplicated() {
        UserCreateRequest request = new UserCreateRequest(email, name);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        Assertions.assertThrows(UserEmailExistException.class, () -> sut.createUser(request));
    }

    @DisplayName("UserService 사용자 생성 - 유저네임 중복 실패")
    @Test
    public void createUserFailByUsernameDuplicated() {
        UserCreateRequest request = new UserCreateRequest(email, name);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByName(name)).thenReturn(true);

        Assertions.assertThrows(UserNameExistException.class, () -> sut.createUser(request));
    }

    @DisplayName("UserService 사용자 정보 수정 - 성공")
    @Test
    public void updateUserDetailSuccess() {
        String description = "updatedDescription";
        UserUpdateRequest request = UserUpdateRequest.builder()
                .description(description).build();
        when(userRepository.findById(nullable(Long.class))).thenReturn(Optional.ofNullable(user));

        User result = sut.updateUserDetail(user, request);

        Assertions.assertEquals(description, result.getDescription());
    }
}
