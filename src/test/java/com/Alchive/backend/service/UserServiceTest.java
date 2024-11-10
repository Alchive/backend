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
                .id(1L)
                .email("user1@test.com")
                .name("user1")
                .build();
    }

    @DisplayName("사용자 생성 - 성공")
    @Test
    void createUser_success() {
        // given
        UserCreateRequest createRequest = UserCreateRequest.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();

        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.validateAccessToken(request)).thenReturn(user.getId());

        // when
        UserResponseDTO returnedUser = userService.createUser(createRequest);

        // then
        assertNotNull(returnedUser);
        assertEquals(user.getId(), returnedUser.getUserId());
        assertEquals(user.getName(), returnedUser.getUserName());
        assertEquals(user.getEmail(), returnedUser.getUserEmail());
    }

    @DisplayName("사용자 생성 - 유저 닉네임 중복")
    @Test
    void createUser_username_exists() {
        // given
        UserCreateRequest createRequest = UserCreateRequest.builder()
                .email("testEmail@test.com")
                .name("duplicatedUserName")
                .build();
        when(userService.isDuplicateUsername(createRequest.getName())).thenReturn(true);

        // when
        boolean returnedAnswer = userService.isDuplicateUsername(createRequest.getName());

        // then
        assertTrue(returnedAnswer);
    }

    @DisplayName("사용자 생성 - 유저 이메일 중복")
    @Test
    void createUser_userEmail_exists() {
        // given
        UserCreateRequest createRequest = UserCreateRequest.builder()
                .email("duplicatedEmail@test.com")
                .name("user1")
                .build();
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        // when, then
        assertThrows(UserEmailExistException.class, () -> userService.createUser(createRequest));
    }

    @DisplayName("프로필 조회 - 성공")
    @Test
    void getUser_success() {
        // given
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.validateAccessToken(request)).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        User returnedUser = userService.getUserDetail(request);

        // then
        assertNotNull(returnedUser);
        assertEquals(user.getId(), returnedUser.getId());
        assertEquals(user.getEmail(), returnedUser.getEmail());
        assertEquals(user.getName(), returnedUser.getName());
    }

    @DisplayName("프로필 조회 - 존재하지 않는 유저 아이디")
    @Test
    void getUser_userNotFound() {
        // given
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.validateAccessToken(request)).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // when, then
        assertThrows(NoSuchUserIdException.class, () -> userService.getUserDetail(request));
    }

    @DisplayName("프로필 수정 - 성공")
    @Test
    void updateUser_success() {
        // given
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .description("updatedUserDescription")
                .build();
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.validateAccessToken(request)).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        userService.updateUserDetail(request, updateRequest);

        // then
        assertNotNull(user);
        assertEquals(user.getDescription(), "updatedUserDescription");
    }

    @DisplayName("사용자 삭제 - 성공")
    @Test
    void deleteUser_success() {
        // given
        doNothing().when(tokenService).validateAccessToken(request);
        when(tokenService.validateAccessToken(request)).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        userService.deleteUserDetail(request);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(NoSuchUserIdException.class, () -> userService.getUserDetail(request));
    }
}
