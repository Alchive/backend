package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.error.exception.user.UserEmailExistException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.UserCreateRequest;
import com.Alchive.backend.dto.request.UserUpdateRequest;
import com.Alchive.backend.dto.response.UserResponseDTO;
import com.Alchive.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private HttpServletRequest request;

    private User user;

    @BeforeEach
    void setUp() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this);

        // 테스트용 Builder 패턴을 이용해 userId까지 직접 입력
        user = User.userTestBuilder()
                .userId(1L)
                .userEmail("user1@test.com")
                .userNickName("user1")
                .build();
    }

    @DisplayName("사용자 생성 - 성공")
    @Test
    void createUser_success() {
        // given
        UserCreateRequest createRequest = UserCreateRequest.builder()
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .build();

        when(userRepository.existsByUserEmail(createRequest.getUserEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());

        // when
        UserResponseDTO returnedUser = userService.createUser(createRequest);

        // then
        assertNotNull(returnedUser);
        assertEquals(user.getUserId(),returnedUser.getUserId());
        assertEquals(user.getUserName(),returnedUser.getUserName());
        assertEquals(user.getUserEmail(),returnedUser.getUserEmail());
    }

    @DisplayName("사용자 생성 - 유저 닉네임 중복")
    @Test
    void createUser_username_exists() {
        // given
        UserCreateRequest createRequest = UserCreateRequest.builder()
                .userEmail("testEmail@test.com")
                .userName("duplicatedUserName")
                .build();
        when(userService.isDuplicateUsername(createRequest.getUserName())).thenReturn(true);

        // when
        boolean returnedAnswer = userService.isDuplicateUsername(createRequest.getUserName());

        // then
        assertTrue(returnedAnswer);
    }

    @DisplayName("사용자 생성 - 유저 이메일 중복")
    @Test
    void createUser_userEmail_exists() {
        // given
        UserCreateRequest createRequest = UserCreateRequest.builder()
                .userEmail("duplicatedEmail@test.com")
                .userName("user1")
                .build();
        when(userRepository.existsByUserEmail(createRequest.getUserEmail())).thenReturn(true);

        // when, then
        assertThrows(UserEmailExistException.class, () -> userService.createUser(createRequest));
    }

    @DisplayName("프로필 조회 - 성공")
    @Test
    void getUser_success() {
        // given
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        User returnedUser = userService.getUserDetail(request);

        // then
        assertNotNull(returnedUser);
        assertEquals(user.getUserId(),returnedUser.getUserId());
        assertEquals(user.getUserEmail(),returnedUser.getUserEmail());
        assertEquals(user.getUserName(),returnedUser.getUserName());
    }

    @DisplayName("프로필 조회 - 존재하지 않는 유저 아이디")
    @Test
    void getUser_userNotFound() {
        // given
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(NoSuchUserIdException.class, () -> userService.getUserDetail(request));
    }

    @DisplayName("프로필 수정 - 성공")
    @Test
    void updateUser_success() {
        // given
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .userDescription("updatedUserDescription")
                .build();
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        userService.updateUserDetail(request,updateRequest);

        // then
        assertNotNull(user);
        assertEquals(user.getUserDescription(),"updatedUserDescription");
    }

    @DisplayName("사용자 삭제 - 성공")
    @Test
    void deleteUser_success() {
        // given
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.getUserIdFromToken(request)).thenReturn(user.getUserId());
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // when
        userService.deleteUserDetail(request);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        // then
        assertThrows(NoSuchUserIdException.class, () -> userService.getUserDetail(request));
    }
}
