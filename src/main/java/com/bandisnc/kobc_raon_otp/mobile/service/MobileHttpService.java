package com.bandisnc.kobc_raon_otp.mobile.service;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MobileHttpService {

    private final RestTemplate restTemplate;

    @Value("${ONE_PASS_URL}")
    private String onePassUrl;

    @Value("${SITE_ID}")
    private String siteId;

    @Value("${SVC_ID}")
    private String svcId;

    @Value("${AUTH_TYPE}")
    private String authType;

    public ResponseDTO sendOnePassAddRequest(String command, String userId, String deviceId) {
        Map<String, String> map = new HashMap<>();
        map.put("command", command);
        map.put("siteId", siteId);
        map.put("svcId", svcId);
        map.put("loginId", userId);
        map.put("deviceId", deviceId);
        map.put("authType", authType);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, header);
        try {
            ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(onePassUrl, entity, ResponseDTO.class);
            ResponseDTO responseDTO = response.getBody();
            if (responseDTO == null) {
                System.out.println("응답이 없습니다.");;
                return null;
            }
            return responseDTO;
        } catch (Exception e) {
            System.err.println("One Pass API 호출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }

    public ResponseDTO sendOnePassAuthRequest( String command, String user_id, String deviceId, String trId) {
        Map<String, String> map = new HashMap<>();

        map.put("command", command);
        map.put("svcTrId", trId);
        map.put("siteId", siteId);
        map.put("svcId", svcId);
        map.put("loginId", user_id);
        map.put("deviceId", deviceId);
        map.put("authType", authType);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, header);

        try {
            ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(onePassUrl, entity, ResponseDTO.class);
            ResponseDTO responseDTO = response.getBody();
            if (responseDTO != null && "000".equals(responseDTO.getResultCode())) {
                ResponseDTO.ResultData resultData = responseDTO.getResultData();
                return responseDTO;
            }
            return responseDTO;
        } catch (Exception e) {
            System.err.println("One Pass API 호출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }


    public ResponseDTO sendOnePassVerifyRequest(String command, String trId, String otpValue) {
        Map<String, String> map = new HashMap<>();
        map.put("command", command);
        map.put("trId", trId);
        map.put("otpValue", otpValue);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, header);

        try {
            ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(onePassUrl, entity, ResponseDTO.class);
            ResponseDTO responseDTO = response.getBody();
            if (responseDTO != null && "000".equals(responseDTO.getResultCode())) {
                ResponseDTO.ResultData resultData = responseDTO.getResultData();
                return responseDTO;
            }
            return responseDTO;
        } catch (Exception e) {
            System.err.println("One Pass API 호출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }


    public ResponseDTO sendOnePassRevokeReQuest(String command, String user_id) {
        Map<String, String> map = new HashMap<>();
        map.put("command", command);
        map.put("siteId", siteId);
        map.put("svcId", svcId);
        map.put("loginId", user_id);
        map.put("authType", authType);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, header);

        try {
            ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(onePassUrl, entity, ResponseDTO.class);
            ResponseDTO responseDTO = response.getBody();
            if (responseDTO != null && "000".equals(responseDTO.getResultCode())) {
                ResponseDTO.ResultData resultData = responseDTO.getResultData();
                return responseDTO;
            }
            return responseDTO;
        } catch (Exception e) {
            System.err.println("One Pass API 호출 중 오류 발생: " + e.getMessage());
            return null;
        }
    }
}

