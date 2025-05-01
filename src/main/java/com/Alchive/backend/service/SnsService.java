package com.Alchive.backend.service;

import com.Alchive.backend.config.error.exception.sns.InvalidGrantException;
import com.Alchive.backend.config.error.exception.sns.NoSuchSnsIdException;
import com.Alchive.backend.domain.sns.Sns;
import com.Alchive.backend.domain.user.User;
import com.Alchive.backend.dto.request.SnsCreateRequest;
import com.Alchive.backend.dto.response.SnsResponseDTO;
import com.Alchive.backend.repository.SnsReporitory;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class SnsService {
    private final SnsReporitory snsReporitory;

    public SnsResponseDTO getSns(Long snsId) {
        return new SnsResponseDTO(snsReporitory.findById(snsId)
                .orElseThrow(NoSuchSnsIdException::new));
    }

    @Transactional
    public void createSns(User user, SnsCreateRequest request) {
        Sns sns = Sns.of(user, request);
        snsReporitory.save(sns);
    }

    private RestTemplate restTemplate = new RestTemplate();
    protected ResponseEntity<Map> sendRestTemplateExchange(String requestUrl, HttpMethod method, HttpEntity request) {
        ResponseEntity<Map> response = restTemplate.exchange(requestUrl, method, request, Map.class);
        return response;
    }

    protected Object parseResponse(ResponseEntity<Map> response, String targetParam) {
        Map<String, Object> responseBody = response.getBody();
        checkInvalidGrant(responseBody);
        Object targetResult = responseBody.get(targetParam);
        return targetResult;
    }

    private void checkInvalidGrant(Map<String, Object> responseBody) {
        if ((responseBody.containsKey("error") && responseBody.get("error").equals("invalid_grant")) || (responseBody.containsKey("ok") && responseBody.get("ok").equals("false"))) {
            throw new InvalidGrantException();
        }
    }
}
