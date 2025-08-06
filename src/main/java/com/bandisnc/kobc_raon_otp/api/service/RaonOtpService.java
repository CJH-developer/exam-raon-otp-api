package com.bandisnc.kobc_raon_otp.api.service;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("RaonOtpService")
@RequiredArgsConstructor
public class RaonOtpService {

    @Autowired
    private final RaonOtpHttpService raonOtpHttpService;


    /**
     * 장비 등록
     * @param user_id 사용자 ID
     * @param deviceId 장비 ID
     * @return
     */
    public ResponseDTO add(String user_id, String deviceId) {
        return raonOtpHttpService.sendOnePassAddRequest(user_id, deviceId);
    }

    /**
     * OTP 서비스 인증
     * @param user_id 사용자 ID
     * @param deviceId 장비 ID
     * @param trId 서비스 ID
     * @return
     */
    public ResponseDTO auth(String user_id, String deviceId, String trId) {
        return raonOtpHttpService.sendOnePassAuthRequest(user_id, deviceId, trId);
    }

    /**
     * OTP 번호 검증
     * @param trId 서비스 ID
     * @param otpValue OTP 번호
     * @return
     */
    public ResponseDTO verify(String trId, String otpValue) {
        return raonOtpHttpService.sendOnePassVerifyRequest(trId, otpValue);
    }

    /**
     * OTP 해제
     * @param user_id 사용자 ID
     * @return
     */
    public ResponseDTO revoke(String user_id) {
        return raonOtpHttpService.sendOnePassRevokeReQuest(user_id);
    }
}
