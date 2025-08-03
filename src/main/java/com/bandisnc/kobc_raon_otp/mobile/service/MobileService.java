package com.bandisnc.kobc_raon_otp.mobile.service;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MobileService {

    private final MobileHttpService mobileHttpService;

    @Value("${ONE_PASS_ADD_COMMAND}")
    private String regist;

    @Value("${ONE_PASS_AUTH_COMMAND}")
    private String auth;

    @Value("${ONE_PASS_VERIFY_COMMAND}")
    private String check;

    @Value("${ONE_PASS_REVOKE_COMMAND}")
    private String revoke;

    public ResponseDTO add(String user_id, String deviceId) {
        return mobileHttpService.sendOnePassAddRequest(regist, user_id, deviceId);
    }

    public ResponseDTO auth(String user_id, String deviceId, String trId) {
        return mobileHttpService.sendOnePassAuthRequest(auth, user_id, deviceId, trId);
    }

    public ResponseDTO verify(String trId, String otpValue) {
        return mobileHttpService.sendOnePassVerifyRequest(check, trId, otpValue);
    }

    public ResponseDTO revoke(String user_id) {
        return mobileHttpService.sendOnePassRevokeReQuest(revoke, user_id);
    }
}
