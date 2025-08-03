package com.bandisnc.kobc_raon_otp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseDTO {
    private String resultCode;
    private String resultMsg;
    private ResultData resultData;
    /**
     * 응답메세지 내의 데이터
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ResultData{
        private String trId;
        private String qrImage;
        private String firstUniqueKey;
        private String avlblTime;
    }
}
