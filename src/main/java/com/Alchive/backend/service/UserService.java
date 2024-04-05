package com.Alchive.backend.service;

import com.Alchive.backend.config.Code;
import com.Alchive.backend.config.exception.NoSuchIdException;
import com.Alchive.backend.config.exception.NoSuchPlatformException;
import com.Alchive.backend.domain.User;
import com.Alchive.backend.dto.request.UserCreateRequest;
import com.Alchive.backend.dto.request.UserUpdateRequest;
import com.Alchive.backend.dto.response.UserResponseDTO;
import com.Alchive.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserCreateRequest request) {
        String email = request.getUserEmail();
        String username = request.getUserName();
        if (userRepository.existsByUserEmail(email)) { // 중복 이메일 검사
            throw new NoSuchPlatformException(Code.USER_EMAIL_EXISTS, email);
        }
        if (userRepository.existsByUserName(username)) { // 중복 유저 이름 검사
            throw new NoSuchPlatformException(Code.USER_NAME_EXISTS, username);
        }

        User user = new User(); // 유저 생성
        user.setUserEmail(email);
        user.setUserName(username);
        user.setCreatedAt(new Date());
        user = userRepository.save(user); // db에 유저 저장 - 회원 가입
        return new UserResponseDTO(user);
    }

    public void isDuplicateUsername(String userName) {
        if (userRepository.existsByUserName(userName)) {
            throw new NoSuchPlatformException(Code.USER_NAME_EXISTS, userName);
        }
    }

    public User getUserDetail(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchIdException(Code.USER_NOT_FOUND, userId));
    }

    @Transactional
    public void updateUserDetail(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchIdException(Code.USER_NOT_FOUND, userId));
        user.update(request.getUserDescription(), request.getAutoSave());
    }

    @Transactional
    public void deleteUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchIdException(Code.USER_NOT_FOUND, userId));
        userRepository.delete(user);
    }
}