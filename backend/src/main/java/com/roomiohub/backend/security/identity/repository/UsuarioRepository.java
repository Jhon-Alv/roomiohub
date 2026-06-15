package com.roomiohub.backend.security.identity.repository;

import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface UsuarioRepository extends BaseJpaRepository<UsuarioEntity, UUID>, JpaSpecificationExecutor<UsuarioEntity> {

    Optional<UsuarioEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<UsuarioEntity> findByUsernameAndActivoTrue(String username);

    Optional<UsuarioEntity> findByUsernameAndTenantIdAndActivoTrue(String username, UUID tenantId);

    Boolean existsByUsernameAndTenantId(String username, UUID tenantId);

    List<UsuarioEntity> findAllBySedeId(UUID sedeId);

    List<UsuarioEntity> findAllByActivoTrue();
}
