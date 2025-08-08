package com.bandisnc.kobc_raon_otp.api.controller;

import com.bandisnc.kobc_raon_otp.common.dto.OtpResult;
import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.common.entity.RequestEntity;
import com.bandisnc.kobc_raon_otp.common.repository.RequestRepository;
import com.bandisnc.kobc_raon_otp.common.service.OtpService;
import com.bandisnc.kobc_raon_otp.http.service.HttpService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class RaonOtpController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private final HttpService httpService;

    private final RequestRepository requestRepository;

    @Autowired
    private final OtpService otpService;

    @Value("${html.index.form.page}")
    private String indexPage;

    @Value("${html.login.process.page}")
    private String loginProcessPage;

    /**
     * 장비 동록 
     * @param deviceId
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestParam String deviceId, HttpSession session, Model model){
        String user_id = (String) session.getAttribute("s_id");
        OtpResult result = otpService.addDevice(deviceId, user_id);
        if(result.isSuccess()){
            logger.info("[KOBC_RAON_DEVICE_ADD_SUCCESS]" + result.getMsg());
            model.addAttribute("step", result.getStep());
            model.addAttribute("msg", result.getMsg());
            return indexPage;
        }else{
            model.addAttribute("step", result.getStep());
            model.addAttribute("msg", result.getMsg());
            return indexPage;
        }
    }


    /**
     * OTP 서비스 인증
     * @param otpValue
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/verify")
    public String verify(@RequestParam String otpValue, HttpSession session, Model model){
        String user_id = (String) session.getAttribute("s_id");

        OtpResult result = otpService.verfiyOtp(user_id, otpValue);

        // OTP 검증 성공
        if(result.isSuccess() && result.getStep() == null){
            logger.info("[KOBC_RAON_OTP_VERFIY_SUCCESS]" + result.getMsg());
            model.addAttribute("step", result.getStep());
            model.addAttribute("msg", result.getMsg());
            return loginProcessPage;
        }else{
            logger.info("[KOBC_RAON_OTP_VERFIY_FAIL]" + result.getMsg());
            return indexPage;
        }
    }


    /**
     * OTP 서비스 해제
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/revoke")
    public String revoke(HttpSession session, Model model){
        String user_id = (String) session.getAttribute("s_id");
        OtpResult result = otpService.revokeOtp(user_id);

        model.addAttribute("step", result.getStep());
        model.addAttribute("msg", result.getMsg());

        if(result.isSuccess()){
            logger.info("[KOBC_RAON_OTP_REVOKE_SUCCESS]" + result.getMsg());
            return indexPage;
        }else{
            logger.info("[KOBC_RAON_OTP_REVOKE_FAIL]" + result.getMsg());
            return loginProcessPage;
        }
    }

}
