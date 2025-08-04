package com.bandisnc.kobc_raon_otp.api.controller;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.api.service.RaonOtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RaonOtpApiController {

    private final RaonOtpService raonOtpService;

    /**
     * 장비 등록
     * @param loginId 사용자 ID
     * @param deviceId 장비 ID
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> add(@RequestParam String loginId, @RequestParam String deviceId){
        ResponseDTO responseDTO = raonOtpService.add(loginId, deviceId);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * OTP 서비스 인증
     * @param loginId 사용자 ID
     * @param deviceId 장비 ID
     * @param trId 서비스 ID
     * @return
     */
    @PostMapping("/auth")
    public ResponseEntity<ResponseDTO> auth(@RequestParam String loginId, @RequestParam String deviceId, @RequestParam String trId) {
        ResponseDTO responseDTO = raonOtpService.auth(loginId, deviceId, trId);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * OTP 번호 검증
     * @param trId 서비스 ID
     * @param otpValue OTP 번호
     * @return
     */
    @PostMapping("/verify")
    public ResponseEntity<ResponseDTO> verify( @RequestParam String trId, @RequestParam String otpValue) {
        ResponseDTO responseDTO = raonOtpService.verify(trId, otpValue);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * OTP 서비스 해제
     * @param loginId 사용자 ID
     * @return
     */
    @PostMapping("/revoke")
    public ResponseEntity<ResponseDTO> revoke(@RequestParam String loginId) {
        ResponseDTO responseDTO = raonOtpService.revoke(loginId);
        return ResponseEntity.ok(responseDTO);
    }

}
