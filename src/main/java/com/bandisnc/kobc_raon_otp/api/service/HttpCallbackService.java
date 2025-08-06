package com.bandisnc.kobc_raon_otp.api.service;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HttpCallbackService {

    @Value("${ONE_PASS_URL}")
    private String onePassUrl;

    private final RestTemplate restTemplate;

    public ResponseDTO callback(Map<String, String> map) {

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, header);

        try {
            ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(onePassUrl, httpEntity, ResponseDTO.class);
            ResponseDTO responseDTO = response.getBody();

            if (responseDTO != null && "000".equals(responseDTO.getResultCode())) {
                return responseDTO;
            }
            return responseDTO;
        } catch (Exception e) {
            System.err.println("One Pass API 호출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }

}
