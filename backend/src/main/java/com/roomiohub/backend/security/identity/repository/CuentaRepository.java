package com.roomiohub.backend.security.identity.repository;

import com.roomiohub.backend.security.identity.model.CuentaEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CuentaRepository extends BaseJpaRepository<CuentaEntity, UUID> {

    Boolean existsByRuc(String ruc);

    java.util.Optional<CuentaEntity> findByRuc(String ruc);

}
