package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.token.UnmatchedUserIdException;
import com.Alchive.backend.config.error.exception.user.NoSuchUserIdException;
import com.Alchive.backend.config.error.exception.user.UserEmailExistException;
import com.Alchive.backend.config.error.exception.user.UserNameExistException;
import com.Alchive.backend.config.jwt.JwtTokenProvider;
import com.Alchive.backend.config.redis.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public UserResponseDTO createUser(UserCreateRequest request) {
        String email = request.getEmail();
        String username = request.getName();

        isDuplicatedEmailUsername(email, username);
        User savedUser = createSaveUser(email, username);
        String accessToken = createUserTokens(email);

        return new UserResponseDTO(savedUser, accessToken);
    }

    private void isDuplicatedEmailUsername(String email, String username) {
        if (isDuplicatedEmail(email)) {
            throw new UserEmailExistException();
        }
        if (isDuplicatedUsername(username)) {
            throw new UserNameExistException();
        }
    }

    private boolean isDuplicatedEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isDuplicatedUsername(String name) {
        return userRepository.existsByName(name);
    }

    private User createSaveUser(String email, String username) {
        User user = new User(email, username);
        return userRepository.save(user);
    }

    public String createUserTokens(String email) {
        String accessToken = jwtTokenProvider.createAccessToken(email);
        String refreshToken = refreshTokenService.createRefreshToken(email);
        refreshTokenService.saveRefreshToken(email, refreshToken);
        return accessToken;
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
        refreshTokenService.deleteRefreshToken(user.getEmail());
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