package com.bandisnc.kobc_raon_otp.api.controller;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.common.entity.RequestEntity;
import com.bandisnc.kobc_raon_otp.common.repository.RequestRepository;
import com.bandisnc.kobc_raon_otp.api.service.RaonOtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class RaonOtpController {

    private final RaonOtpService raonOtpService;

    private final RequestRepository requestRepository;

    @PostMapping("/add")
    public String add(@RequestParam String deviceId, HttpSession session, Model model){
        String user_id = (String) session.getAttribute("s_id");
        ResponseDTO responseDTO = raonOtpService.add(user_id, deviceId);
        
        // 장비 등록 성공 시 입력한 deviceId 저장 및 응답메세지의 TrId 저장
        if(responseDTO.getResultCode().equals("000")) {
            RequestEntity requestEntity = requestRepository.findById(user_id).orElse(null);
            if (requestEntity != null) {
                requestEntity.setDeviceId(deviceId);
                requestEntity.setTrId(responseDTO.getResultData().getTrId());
                requestRepository.save(requestEntity);
            }
            ResponseDTO authReuslt = raonOtpService.auth(user_id, deviceId, responseDTO.getResultData().getTrId());
            if(authReuslt.getResultCode().equals("100")){
                model.addAttribute("step", "otp");
                return "index";
                // 2.2 인증 실패 시, 장비 재등록
            }else{
                model.addAttribute("step", "device");
                model.addAttribute("msg", "OTP 서비스 인증에 실패하였습니다. 장비를 다시 등록해주세요.");
                return "index";
            }
        }else{
            model.addAttribute("msg", responseDTO.getResultMsg());
            model.addAttribute("step", "device");
            return "index";
        }
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String otpValue, HttpSession session, Model model){
        String user_id = (String) session.getAttribute("s_id");
        RequestEntity requestEntity = requestRepository.findById(user_id).orElse(null);
        String trId = requestEntity.getTrId();

        ResponseDTO responseDTO = raonOtpService.verify(trId, otpValue);
        if(responseDTO.getResultCode().equals("000")) {
            return "loginProcess";
        }else{
            model.addAttribute("msg", responseDTO.getResultMsg());
            model.addAttribute("step", "otp");
            return "index";
        }
    }

    @PostMapping("/revoke")
    public String revoke(HttpSession session, Model model){
        String user_id = (String) session.getAttribute("s_id");
        ResponseDTO responseDTO = raonOtpService.revoke(user_id);
        if(responseDTO.getResultCode().equals("000")) {
            model.addAttribute("step", "device");
            model.addAttribute("msg", "장비를 등록해야 서비스 이용이 가능합니다.");
            return "index";
        }else{
            model.addAttribute("msg", responseDTO.getResultMsg());
            return "loginProcess";
        }
    }

}
