package com.Alchive.backend.service;

import com.Alchive.backend.repository.AlgorithmProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlgorithmProblemService {

    private final AlgorithmProblemRepository algorithmProblemRepository;

}
