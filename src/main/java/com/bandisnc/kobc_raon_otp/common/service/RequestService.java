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
        if(requestDTO.getId() == null || requestDTO.getId().trim().isEmpty()){
            throw new IllegalArgumentException("아이디가 없습니다.");
        }

        Optional<RequestEntity> findUser = requestRepository.findById(requestDTO.getId());
        if(!findUser.isPresent()){
            return null;
        }

        RequestEntity requestEntity = findUser.get();
        if(!requestEntity.getPassword().equals(requestDTO.getPassword())){
            return null;
        }
        return RequestDTO.toRequestDTO(requestEntity);
    }

}
