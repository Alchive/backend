package com.Alchive.backend.service;

import com.Alchive.backend.config.Code;
import com.Alchive.backend.config.ErrorCode;
import com.Alchive.backend.config.GeneralException;
import com.Alchive.backend.domain.User;
import com.Alchive.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserDetailService {
    private final UserRepository userRepository;

    public User getUserDetail(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(Code.RESOURCE_NOT_FOUND,userId));
    }
}