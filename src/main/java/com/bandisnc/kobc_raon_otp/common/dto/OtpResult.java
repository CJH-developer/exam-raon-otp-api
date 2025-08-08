package com.bandisnc.kobc_raon_otp.common.dto;

import lombok.Getter;

@Getter
public class OtpResult {

    private String step;
    private String msg;
    private boolean success;

    public OtpResult(String step, String msg, boolean success) {
        this.step = step;
        this.msg = msg;
        this.success = success;
    }
}
