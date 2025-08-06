package com.bandisnc.kobc_raon_otp.http.service;

import com.bandisnc.kobc_raon_otp.common.dto.RequestDTO;
import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service("RaonOtpService")
@RequiredArgsConstructor
public class HttpService {

    @Autowired
    private final HttpRequestService httpRequestService;


    /**
     * 장비 등록
     * @param user_id 사용자 ID
     * @param deviceId 장비 ID
     * @return
     */
    public ResponseDTO add(String user_id, String deviceId) {
        return httpRequestService.sendOnePassAddRequest(user_id, deviceId);
    }


    /**
     * OTP 서비스 인증
     * @param user_id 사용자 ID
     * @param deviceId 장비 ID
     * @param trId 서비스 ID
     * @return
     */
    public ResponseDTO auth(String user_id, String deviceId, String trId) {
        return httpRequestService.sendOnePassAuthRequest(user_id, deviceId, trId);
    }

    /**
     * OTP 번호 검증
     * @param trId 서비스 ID
     * @param otpValue OTP 번호
     * @return
     */
    public ResponseDTO verify(String trId, String otpValue) {
        return httpRequestService.sendOnePassVerifyRequest(trId, otpValue);
    }

    /**
     * OTP 해제
     * @param user_id 사용자 ID
     * @return
     */
    public ResponseDTO revoke(String user_id) {
        return httpRequestService.sendOnePassRevokeReQuest(user_id);
    }
}
