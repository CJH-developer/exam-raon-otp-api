package com.bandisnc.kobc_raon_otp.common.service;

import com.bandisnc.kobc_raon_otp.common.dto.OtpResult;
import com.bandisnc.kobc_raon_otp.common.dto.RequestDTO;
import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.http.service.HttpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class LoginProcessService {

    private final RequestService  requestService;

    private final HttpService httpService;

    public OtpResult login(RequestDTO requestDTO, HttpSession session) {

        RequestDTO result = requestService.login(requestDTO);

        if (result == null) {
            return new OtpResult("login", "로그인에 실패하였습니다.", false);
        }

        session.setAttribute("s_id", result.getId());

        if (result.getDeviceId() == null || result.getDeviceId().equals("")) {
            return new OtpResult("device", null, false);
        }

        // 로그인 성공 -> 서비스 인증 프로세스
        ResponseDTO authReuslt = httpService.auth(result.getId(), result.getDeviceId(), result.getTrId());

        if ("000".equals(authReuslt.getResultCode())) {
            return new OtpResult("otp", null, true);
        } else {
            return new OtpResult("device", "OTP 서비스 인증에 실패하였습니다. 장비를 다시 등록해주세요.", false);
        }
    }
}
