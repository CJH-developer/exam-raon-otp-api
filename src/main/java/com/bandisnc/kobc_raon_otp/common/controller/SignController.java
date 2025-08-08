package com.bandisnc.kobc_raon_otp.common.controller;

import com.bandisnc.kobc_raon_otp.common.dto.RequestDTO;
import com.bandisnc.kobc_raon_otp.common.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignController {

    private final RequestService requestService;

    @Value("${html.index.form.page}")
    private String indexPage;

    @PostMapping("/signForm")
    public String signForm(RequestDTO requestDTO) {
        requestService.save(requestDTO);
        return indexPage;
    }

}
