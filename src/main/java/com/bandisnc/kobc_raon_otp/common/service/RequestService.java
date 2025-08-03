package com.bandisnc.kobc_raon_otp.common.service;

import com.bandisnc.kobc_raon_otp.common.dto.RequestDTO;
import com.bandisnc.kobc_raon_otp.common.entity.RequestEntity;
import com.bandisnc.kobc_raon_otp.common.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    public void save(RequestDTO requestDTO) {
        RequestEntity requestEntity = RequestEntity.toRequestEntity(requestDTO);
        requestRepository.save(requestEntity);
    }

    public RequestDTO login(RequestDTO requestDTO) {
        Optional<RequestEntity> findUser = requestRepository.findById(requestDTO.getId());
        if(findUser.isPresent()) {
            RequestEntity requestEntity = findUser.get();
            if(requestEntity.getPassword().equals(requestDTO.getPassword())) {
                return RequestDTO.toRequestDTO(requestEntity);
            }else return null;
        }else return null;
    }

}
