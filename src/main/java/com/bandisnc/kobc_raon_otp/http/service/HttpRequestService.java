package com.bandisnc.kobc_raon_otp.http.service;

import com.bandisnc.kobc_raon_otp.common.dto.RequestDTO;
import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service(value = "RaonOtpHttpService")
@RequiredArgsConstructor
public class HttpRequestService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HttpCallbackService httpCallbackService;

    @Value("${SITE_ID}")
    private String siteId;

    @Value("${SVC_ID}")
    private String svcId;

    @Value("${AUTH_TYPE}")
    private String authType;

    @Value("${ONE_PASS_ADD_COMMAND}")
    private String regist;

    @Value("${ONE_PASS_AUTH_COMMAND}")
    private String auth;

    @Value("${ONE_PASS_VERIFY_COMMAND}")
    private String check;

    @Value("${ONE_PASS_REVOKE_COMMAND}")
    private String revoke;

    /**
     * 장비 등록
     * @param userId 사용자 ID
     * @param deviceId 장비 ID
     * @return
     */
    public ResponseDTO sendOnePassAddRequest(String userId, String deviceId) {
        Map<String, String> map = new HashMap<>();
        map.put("command", regist);
        map.put("siteId", siteId);
        map.put("svcId", svcId);
        map.put("loginId", userId);
        map.put("deviceId", deviceId);
        map.put("authType", authType);

        return httpCallbackService.callback(map);
    }

    /**
     * OTP 서비스 인증
     * @param user_id 사용자 ID
     * @param deviceId 장비 ID
     * @param trId 서비스 ID
     * @return
     */
    public ResponseDTO sendOnePassAuthRequest(String user_id, String deviceId, String trId) {
        Map<String, String> map = new HashMap<>();

        map.put("command", auth);
        map.put("svcTrId", trId);
        map.put("siteId", siteId);
        map.put("svcId", svcId);
        map.put("loginId", user_id);
        map.put("deviceId", deviceId);
        map.put("authType", authType);

        return httpCallbackService.callback(map);
    }

    /**
     * OTP 번호 검증
     * @param trId 서비스 ID
     * @param otpValue OTP 번호
     * @return
     */
    public ResponseDTO sendOnePassVerifyRequest(String trId, String otpValue) {
        Map<String, String> map = new HashMap<>();
        map.put("command", check);
        map.put("trId", trId);
        map.put("otpValue", otpValue);

        return httpCallbackService.callback(map);
    }

    /**
     * OTP 서비스 해제
     * @param user_id 사용자 ID
     * @return
     */
    public ResponseDTO sendOnePassRevokeReQuest(String user_id) {
        Map<String, String> map = new HashMap<>();
        map.put("command", revoke);
        map.put("siteId", siteId);
        map.put("svcId", svcId);
        map.put("loginId", user_id);
        map.put("authType", authType);

        return httpCallbackService.callback(map);
    }



}

