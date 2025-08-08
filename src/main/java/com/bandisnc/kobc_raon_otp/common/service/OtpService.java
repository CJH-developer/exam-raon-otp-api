package com.bandisnc.kobc_raon_otp.common.service;

import com.bandisnc.kobc_raon_otp.common.dto.OtpResult;
import com.bandisnc.kobc_raon_otp.common.dto.ResponseDTO;
import com.bandisnc.kobc_raon_otp.common.entity.RequestEntity;
import com.bandisnc.kobc_raon_otp.common.repository.RequestRepository;
import com.bandisnc.kobc_raon_otp.http.service.HttpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final HttpService httpService;

    private final RequestRepository requestRepository;

    public OtpResult addDevice(String user_id, String deviceId){

        ResponseDTO responseDTO = httpService.add(user_id,deviceId);

        // 장비 등록 실패
        if(!responseDTO.getResultCode().equals("000")){
            return new OtpResult("device", responseDTO.getResultMsg(), false);
        }

        RequestEntity requestEntity = requestRepository.findById(user_id).orElse(null);
        if(requestEntity != null){
            requestEntity.setDeviceId(deviceId);
            requestEntity.setTrId(responseDTO.getResultData().getTrId());
            requestRepository.save(requestEntity);
        }
        
        
        ResponseDTO authReuslt = httpService.auth(user_id, deviceId, responseDTO.getResultData().getTrId());
        // 서비스 인증 성공
        if("000".equals(authReuslt.getResultCode())){
            String msg = "장비 등록 여부 : " + responseDTO.getResultMsg() + " / 서비스 인증 여부 : " + authReuslt.getResultMsg();
            return new OtpResult("otp", msg, true);
        }else{
            String msg = "장비 등록 여부 : " + responseDTO.getResultMsg() + " / 서비스 인증 여부 : " + authReuslt.getResultMsg();
            return new OtpResult("device", msg, false);
        }
    }

    public OtpResult verfiyOtp(String user_id, String otpValue){

        RequestEntity requestEntity = requestRepository.findById(user_id).orElse(null);
        
        if(requestEntity == null){
            return new OtpResult("login", "세션이 만료되었습니다.", false);
        }

        String trId = requestEntity.getTrId();
        if(trId == null || trId.isEmpty()){
            return new OtpResult("device", "서비스 거래ID가 없습니다. 장비를 재등록해주세요.", false);
        }

        ResponseDTO responseDTO = httpService.verify(user_id, otpValue);
        // OTP 검증 성공 시
        if("000".equals(responseDTO.getResultCode())){
            return new OtpResult(null, null, true);
        }else if("102".equals(responseDTO.getResultCode())){
            return new OtpResult("device", responseDTO.getResultMsg() + " / 거래 ID 발급을 위해 장비를 다시 등록해주세요.", false);
        }else{
            return new OtpResult("otp", responseDTO.getResultMsg(), false);
        }
    }

    public OtpResult revokeOtp(String user_id){
        ResponseDTO responseDTO = httpService.revoke(user_id);
        if("000".equals(responseDTO.getResultCode())){
            return new OtpResult("device", "장비를 등록해야 서비스 이용이 가능합니다.", false);
        }else{
            return new OtpResult("null", responseDTO.getResultMsg(), false);
        }
    }
}
