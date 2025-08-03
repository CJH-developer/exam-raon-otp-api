package com.bandisnc.kobc_raon_otp.mobile.controller;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.mobile.service.MobileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MobileApiController {

    private final MobileService mobileService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> add(@RequestParam String loginId, @RequestParam String deviceId){
        ResponseDTO responseDTO = mobileService.add(loginId, deviceId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/auth")
    public ResponseEntity<ResponseDTO> auth(@RequestParam String loginId, @RequestParam String deviceId, @RequestParam String trId) {
        ResponseDTO responseDTO = mobileService.auth(loginId, deviceId, trId);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseDTO> verify( @RequestParam String trId, @RequestParam String otpValue) {
        ResponseDTO responseDTO = mobileService.verify(trId, otpValue);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/revoke")
    public ResponseEntity<ResponseDTO> revoke(@RequestParam String loginId) {
        ResponseDTO responseDTO = mobileService.revoke(loginId);
        return ResponseEntity.ok(responseDTO);
    }

}
