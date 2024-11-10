package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.token.UnmatchedUserIdException;
import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.error.exception.user.UserEmailExistException;
import com.Alchive.backend.config.error.exception.user.UserNameExistException;
import com.Alchive.backend.config.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserResponseDTO createUser(UserCreateRequest request) {
        String email = request.getEmail();
        String username = request.getName();
        if (userRepository.existsByEmail(email)) { // 중복 이메일 검사
            throw new UserEmailExistException();
        }
        if (userRepository.existsByName(username)) { // 중복 유저 이름 검사
            throw new UserNameExistException();
        }

        User user = new User(email, username);
        user = userRepository.save(user); // db에 유저 저장 - 회원 가입

        // 토큰 생성 후 전달
//        Long userId = user.getId();
//        String accessToken = tokenService.generateAccessToken(userId);
//        String refreshToken = tokenService.generateRefreshToken();
        String accessToken = jwtTokenProvider.createAccessToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);
        return new UserResponseDTO(user, accessToken, refreshToken);
    }

    public boolean isDuplicateUsername(String name) {
        return userRepository.existsByName(name);
    }

    public User getUserDetail(HttpServletRequest tokenRequest) {
        Long userId = tokenService.validateAccessToken(tokenRequest);
        return userRepository.findById(userId)
                .orElseThrow(NoSuchUserIdException::new);
    }

    @Transactional
    public User updateUserDetail(HttpServletRequest tokenRequest, UserUpdateRequest updateRequest) {
        Long userId = tokenService.validateAccessToken(tokenRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchUserIdException::new);
        return user.update(updateRequest.getDescription(), updateRequest.getAutoSave());
    }

    @Transactional
    public void deleteUserDetail(HttpServletRequest tokenRequest) {
        Long userId = tokenService.validateAccessToken(tokenRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(NoSuchUserIdException::new);
        userRepository.delete(user);
    }

    public void validateUser(Long userId, Long requestedId) {
        if (requestedId != userId) {
            throw new UnmatchedUserIdException();
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NoSuchUserIdException::new);
    }
}