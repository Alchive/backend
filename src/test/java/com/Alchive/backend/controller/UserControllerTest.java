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
    @InjectMocks
    private UserController sut;
    @Mock
    private UserService userService;
    @BeforeEach
    public void setUp() {
    }
    @DisplayName("UserController 유저네임 중복 - 성공")
    @Test
    public void isDuplicateUsernameSuccess() {
        when(userService.isDuplicatedUsername("testNameUnique")).thenReturn(true);

        ResponseEntity<ResultResponse> result = sut.isDuplicateUsername("testNameUnique");

        Assertions.assertEquals(true, result.getBody().getData());
    }
    @DisplayName("UserController 유저네임 중복 - 실패")
    @Test
    public void isDuplicateUsernameFail() {
        when(userService.isDuplicatedUsername("testNameDuplicated")).thenReturn(false);

        ResponseEntity<ResultResponse> result = sut.isDuplicateUsername("testNameDuplicated");

        Assertions.assertEquals(false, result.getBody().getData());
    }
}
