package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.error.exception.user.UserEmailExistException;
import com.Alchive.backend.config.error.exception.user.UserNameExistException;
import com.Alchive.backend.config.jwt.TokenService;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.UserCreateRequest;
import com.Alchive.backend.dto.request.UserUpdateRequest;
import com.Alchive.backend.dto.response.UserResponseDTO;
import com.Alchive.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Transactional
    public UserResponseDTO createUser(UserCreateRequest request) {
        String email = request.getUserEmail();
        String username = request.getUserName();
        if (userRepository.existsByUserEmail(email)) { // 중복 이메일 검사
            throw new UserEmailExistException();
        }
        if (userRepository.existsByUserName(username)) { // 중복 유저 이름 검사
            throw new UserNameExistException();
        }

        User user = new User(email,username);
        user = userRepository.save(user); // db에 유저 저장 - 회원 가입

        // 토큰 생성 후 전달
        Long userId = user.getId();
        String accessToken = tokenService.generateAccessToken(userId);
        String refreshToken = tokenService.generateRefreshToken();
        return new UserResponseDTO(user,accessToken,refreshToken);
    }

    public boolean isDuplicateUsername(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public User getUserDetail(HttpServletRequest tokenRequest) {
        tokenService.validateAccessToken(tokenRequest);
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserIdException());
    }

    @Transactional
    public void updateUserDetail(HttpServletRequest tokenRequest, UserUpdateRequest updateRequest) {
        tokenService.validateAccessToken(tokenRequest);
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserIdException());
        user.update(updateRequest.getUserDescription(), updateRequest.getAutoSave());
    }

    @Transactional
    public void deleteUserDetail(HttpServletRequest tokenRequest) {
        tokenService.validateAccessToken(tokenRequest);
        Long userId = tokenService.getUserIdFromToken(tokenRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserIdException());
        userRepository.delete(user);
    }
}