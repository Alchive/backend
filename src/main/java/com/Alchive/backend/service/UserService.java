package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.token.UnmatchedUserIdException;
import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.error.exception.user.UserEmailExistException;
import com.Alchive.backend.config.error.exception.user.UserNameExistException;
import com.Alchive.backend.config.jwt.JwtTokenProvider;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.UserCreateRequest;
import com.Alchive.backend.dto.request.UserUpdateRequest;
import com.Alchive.backend.dto.response.UserResponseDTO;
import com.Alchive.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserResponseDTO createUser(UserCreateRequest request) {
        String email = request.getEmail();
        String username = request.getName();
        // 중복 이메일 검사
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailExistException();
        }
        // 중복 유저 이름 검사
        if (userRepository.existsByName(username)) {
            throw new UserNameExistException();
        }
        // db에 유저 저장 - 회원 가입
        User user = new User(email, username);
        user = userRepository.save(user);
        // 토큰 생성 후 전달
        String accessToken = jwtTokenProvider.createAccessToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);
        return new UserResponseDTO(user, accessToken, refreshToken);
    }

    public boolean isDuplicateUsername(String name) {
        return userRepository.existsByName(name);
    }

    public User getUserDetail(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(NoSuchUserIdException::new);
    }

    @Transactional
    public User updateUserDetail(User user, UserUpdateRequest updateRequest) {
        User updatedUser = userRepository.findById(user.getId())
                .orElseThrow(NoSuchUserIdException::new);
        return updatedUser.update(updateRequest.getDescription(), updateRequest.getAutoSave());
    }

    @Transactional
    public void deleteUserDetail(User user) {
        userRepository.delete(user);
    }

    public void validateUser(Long userId, Long requestedId) {
        if (!Objects.equals(requestedId, userId)) {
            throw new UnmatchedUserIdException();
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NoSuchUserIdException::new);
    }
}