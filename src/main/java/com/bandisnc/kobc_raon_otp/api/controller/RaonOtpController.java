package com.bandisnc.kobc_raon_otp.api.controller;

import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.common.entity.RequestEntity;
import com.bandisnc.kobc_raon_otp.common.repository.RequestRepository;
import com.bandisnc.kobc_raon_otp.http.service.HttpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final HttpService httpService;

    private final RequestRepository requestRepository;

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

        ResponseDTO responseDTO = httpService.add(user_id, deviceId);

        // 장비 등록 성공 시 입력한 deviceId 저장 및 응답메세지의 TrId 저장
        if(responseDTO.getResultCode().equals("000")) {

            RequestEntity requestEntity = requestRepository.findById(user_id).orElse(null);

            if (requestEntity != null) {
                requestEntity.setDeviceId(deviceId);
                requestEntity.setTrId(responseDTO.getResultData().getTrId());
                requestRepository.save(requestEntity);
            }

            ResponseDTO authReuslt = httpService.auth(user_id, deviceId, responseDTO.getResultData().getTrId());

            if(authReuslt.getResultCode().equals("000")){
                model.addAttribute("step", "otp");
                return "index";

            // 2.2 인증 실패 시, 장비 재등록
            }else{
                model.addAttribute("step", "device");
                model.addAttribute("msg", responseDTO.getResultMsg());
                return "index";
            }
        }else{
            model.addAttribute("msg", responseDTO.getResultMsg());
            model.addAttribute("step", "device");
            return "index";
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

        RequestEntity requestEntity = requestRepository.findById(user_id).orElse(null);
        if(requestEntity == null){
            model.addAttribute("step", "login");
            return "redirect:/";
        }

        if(requestEntity.getTrId().isEmpty()){
            //httpService.add(user_id, requestEntity.getDeviceId());
        }
        String trId = requestEntity.getTrId();

        ResponseDTO responseDTO = httpService.verify(trId, otpValue);
        if(responseDTO.getResultCode().equals("000")) {
            return "loginProcess";
        }else if(responseDTO.getResultCode().equals("102")){
            model.addAttribute("msg", responseDTO.getResultMsg());
            model.addAttribute("step", "device");
            return "index";
        }else{
            model.addAttribute("msg", responseDTO.getResultMsg());
            model.addAttribute("step", "otp");
            return "index";
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
        ResponseDTO responseDTO = httpService.revoke(user_id);
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
