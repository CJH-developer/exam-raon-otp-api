package com.bandisnc.kobc_raon_otp.common.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Value("${html.index.form.page}")
    private String indexPage;

    @GetMapping("/")
    public String index() {
        return indexPage;
    }
}
