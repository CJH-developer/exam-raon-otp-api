package com.bandisnc.kobc_raon_otp.common.repository;

import com.bandisnc.kobc_raon_otp.common.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, String> {

    Optional<RequestEntity> findById(String id);
}
