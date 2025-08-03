package com.bandisnc.kobc_raon_otp.common.dto;

import com.bandisnc.kobc_raon_otp.common.entity.RequestEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestDTO {
    private String id;
    private String password;
    private String name;
    private String trId;
    private String deviceId;

    public static RequestDTO toRequestDTO(RequestEntity requestEntity) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setId(requestEntity.getId());
        requestDTO.setPassword(requestEntity.getPassword());
        requestDTO.setName(requestEntity.getName());
        requestDTO.setTrId(requestEntity.getTrId());
        requestDTO.setDeviceId(requestEntity.getDeviceId());
        return requestDTO;
    }
}
