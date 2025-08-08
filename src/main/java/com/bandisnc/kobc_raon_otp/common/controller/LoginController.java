package com.bandisnc.kobc_raon_otp.common.controller;

import com.bandisnc.kobc_raon_otp.common.dto.OtpResult;
import com.bandisnc.kobc_raon_otp.common.dto.RequestDTO;
import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.common.service.LoginProcessService;
import com.bandisnc.kobc_raon_otp.common.service.RequestService;
import com.bandisnc.kobc_raon_otp.http.service.HttpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final LoginProcessService  loginProcessService;

    @Value("${html.index.form.page}")
    private String indexPage;

    @Value("${html.redirect.page}")
    private String redirectUrl;
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
        OtpResult result = loginProcessService.login(requestDTO, session);
        model.addAttribute("step", result.getStep());

        if(result.getMsg() != null && !result.getMsg().isEmpty() ){
            model.addAttribute("msg", result.getMsg());
        }

        logger.info("[LOGIN_SUCCESS] user : " + requestDTO.getId());
        return indexPage;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model){
        logger.info("[LOGOUT_SESSION_EXPIRED]" + session.getAttribute("s_id"));
        session.invalidate();
        return "redirect:" + redirectUrl;
    }

}
