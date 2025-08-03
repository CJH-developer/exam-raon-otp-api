package com.bandisnc.kobc_raon_otp.common.controller;

import com.bandisnc.kobc_raon_otp.common.dto.RequestDTO;
import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.common.service.RequestService;
import com.bandisnc.kobc_raon_otp.mobile.service.MobileService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final RequestService requestService;

    private final MobileService mobileService;

    @PostMapping("/mobile")
    public String loginMobile(RequestDTO requestDTO, HttpSession session, Model model) {
        RequestDTO result = requestService.login(requestDTO);
        // 기본 로그인 성공
        if(result != null){
            session.setAttribute("s_id", result.getId());
            model.addAttribute("step", "login");

            // 1.1 기존에 등록된 장비가 없는 경우, 장비 등록 요청
            if(result.getDeviceId() == null || result.getDeviceId().equals("")){
                model.addAttribute("step", "device");
                return "index";
            // 1.2 기존 등록 장비가 있는 경우, 서비스 인증 요청
            }else{
                ResponseDTO authReuslt = mobileService.auth( result.getDeviceId(), result.getDeviceId(), result.getTrId());
                // 2.1 인증 성공 시, otp 번호 입력 요청
                if(authReuslt.getResultCode().equals("100")){
                    model.addAttribute("step", "otp");
                    return "index";
                // 2.2 인증 실패 시, 장비 재등록
                }else{
                    model.addAttribute("step", "device");
                    model.addAttribute("msg", "OTP 서비스 인증에 실패하였습니다. 장비를 다시 등록해주세요.");
                    return "index";
                }
            }
        }else{
            model.addAttribute("msg", "로그인에 실패하였습니다.");
            return "index";
        }
    }

    @PostMapping("/sms")
    public String loginSms(RequestDTO requestDTO, HttpSession session, Model model) {
        RequestDTO result = requestService.login(requestDTO);

        // 기본 로그인 성공
        if(result != null){
            session.setAttribute("s_id", result.getId());
            model.addAttribute("step", "login");

            // 1.1 기존에 등록된 장비가 없는 경우, 장비 등록 요청
            if(result.getDeviceId() == null || result.getDeviceId().equals("")){
                model.addAttribute("step", "device");
                return "index";
                // 1.2 기존 등록 장비가 있는 경우, 서비스 인증 요청
            }else{
                ResponseDTO authReuslt = mobileService.auth(result.getDeviceId(), result.getDeviceId(), result.getTrId());
                // 2.1 인증 성공 시, otp 번호 입력 요청
                if(authReuslt.getResultCode().equals("100")){
                    model.addAttribute("step", "otp");
                    return "index";
                    // 2.2 인증 실패 시, 장비 재등록
                }else{
                    model.addAttribute("step", "device");
                    model.addAttribute("msg", "OTP 서비스 인증에 실패하였습니다. 장비를 다시 등록해주세요.");
                    return "index";
                }
            }
        }else{
            model.addAttribute("msg", "로그인에 실패하였습니다.");
            return "index";
        }
    }
}
