package com.bandisnc.kobc_raon_otp.common.entity;

import com.bandisnc.kobc_raon_otp.common.dto.RequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="kobc_request")
@Data
@NoArgsConstructor
public class RequestEntity {
    @Id
    private String id;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String trId;
    @Column
    private String deviceId;

    public static RequestEntity toRequestEntity(RequestDTO requestDTO) {
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(requestDTO.getId());
        requestEntity.setPassword(requestDTO.getPassword());
        requestEntity.setName(requestDTO.getName());
        requestEntity.setTrId(requestDTO.getTrId());
        requestEntity.setDeviceId(requestDTO.getDeviceId());
        return requestEntity;
    }
}
