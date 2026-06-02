package com.roomiohub.backend.security.identity.repository;

import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends BaseJpaRepository<UsuarioEntity, UUID> {

    Optional<UsuarioEntity> findByUsernameAndActivoTrue(String username);
}
