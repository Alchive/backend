package com.Alchive.backend.service;

import com.Alchive.backend.domain.User;
import com.Alchive.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService {
    private final UserRepository userRepository;

    public User getUserDetail(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(userId)));
    }
}