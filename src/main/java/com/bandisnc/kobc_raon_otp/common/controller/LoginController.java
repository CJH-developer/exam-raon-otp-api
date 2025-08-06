package com.bandisnc.kobc_raon_otp.common.controller;

import com.bandisnc.kobc_raon_otp.common.dto.RequestDTO;
import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.common.service.RequestService;
import com.bandisnc.kobc_raon_otp.http.service.HttpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RequestService requestService;

    private final HttpService httpService;

    /**
     * 로그인 프로세스
     * 1.1 기본 사용자 로그인
     * 2. 장비 등록 여부 확인
     * 3. 등록된 장비가 없다면 -> 장비 등록 화면 전달
     * 4. 등록된 장비가 있다면 -> OTP 서비스 인증
     * 5. 인증 성공 시 -> OTP 번호 입력 요청
     * 6. 인증 실패 시 -> 장비 재등록 화면 전달
     * 7. 기본 로그인 실패 시 -> 로그인 화면 전달
     */
    @PostMapping("/loginForm")
    public String login(RequestDTO requestDTO, HttpSession session, Model model) {
        RequestDTO result = requestService.login(requestDTO);
        // 1. 기본 로그인 성공
        if(result != null){
            session.setAttribute("s_id", result.getId());
            model.addAttribute("step", "login");

            // 2. 장비 등록 여부 확인
            // 3. 등록된 장비가 없다면 -> 장비 등록 화면 전달
            if(result.getDeviceId() == null || result.getDeviceId().equals("")){
                logger.info("[FAIL_ADD_DEVICE]" +  result.getDeviceId());
                model.addAttribute("step", "device");
                return "index";
            // 4. 등록된 장비가 있다면 -> OTP 서비스 인증
            }else{
                ResponseDTO authReuslt = httpService.auth( result.getId(), result.getDeviceId(), result.getTrId());
                logger.info("[EXIST_DEVICE_CALL_AUTH]" +  authReuslt);
                // 5. 인증 성공 시 -> OTP 번호 입력 요청
                if(authReuslt.getResultCode().equals("000")){
                    logger.info("[SUCCESS_AUTH_INPUT_OTPVALUE]" +  authReuslt.getResultCode() + "/" + authReuslt.getResultMsg());
                    model.addAttribute("step", "otp");
                    return "index";
                // 6. 인증 실패 시 -> 장비 재등록 화면 전달
                }else{
                    logger.info("[FAIL_AUTH_REGIST_DEVICE]" + authReuslt.getResultCode() + "/" + authReuslt.getResultMsg());
                    model.addAttribute("step", "device");
                    model.addAttribute("msg", "OTP 서비스 인증에 실패하였습니다. 장비를 다시 등록해주세요.");
                    return "index";
                }
            }
        }else{
            logger.info("[LOGIN_FAIL]" + session.getAttribute("s_id"));
            model.addAttribute("msg", "로그인에 실패하였습니다.");
            return "index";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model){
        logger.info("[LOGOUT_SESSION_EXPIRED]" + session.getAttribute("s_id"));
        session.invalidate();
        return "redirect:/";
    }

}
