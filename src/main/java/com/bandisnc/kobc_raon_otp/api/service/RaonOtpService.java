package com.bandisnc.kobc_raon_otp.api.service;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RaonOtpService {

    private final RaonOtpHttpService raonOtpHttpService;

    @Value("${ONE_PASS_ADD_COMMAND}")
    private String regist;

    @Value("${ONE_PASS_AUTH_COMMAND}")
    private String auth;

    @Value("${ONE_PASS_VERIFY_COMMAND}")
    private String check;

    @Value("${ONE_PASS_REVOKE_COMMAND}")
    private String revoke;

    public ResponseDTO add(String user_id, String deviceId) {
        return raonOtpHttpService.sendOnePassAddRequest(regist, user_id, deviceId);
    }

    public ResponseDTO auth(String user_id, String deviceId, String trId) {
        return raonOtpHttpService.sendOnePassAuthRequest(auth, user_id, deviceId, trId);
    }

    public ResponseDTO verify(String trId, String otpValue) {
        return raonOtpHttpService.sendOnePassVerifyRequest(check, trId, otpValue);
    }

    public ResponseDTO revoke(String user_id) {
        return raonOtpHttpService.sendOnePassRevokeReQuest(revoke, user_id);
    }
}
