package com.Alchive.backend.controller;

import com.Alchive.backend.config.result.ResultResponse;
import com.Alchive.backend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController sut;
    @BeforeEach
    public void setUp() {
    }
    @DisplayName("유저네임 중복 실패")
    @Test
    public void isDuplicateUsernameFail() {
        when(userService.isDuplicateUsername("testNameDuplicated")).thenReturn(false);

        ResponseEntity<ResultResponse> result = sut.isDuplicateUsername("testNameDuplicated");

        Assertions.assertEquals(false, result.getBody().getData());
    }
    @DisplayName("유저네임 중복 성공")
    @Test
    public void isDuplicateUsernameSuccess() {
        when(userService.isDuplicateUsername("testNameUnique")).thenReturn(true);

        ResponseEntity<ResultResponse> result = sut.isDuplicateUsername("testNameUnique");

        Assertions.assertEquals(true, result.getBody().getData());
    }
}
