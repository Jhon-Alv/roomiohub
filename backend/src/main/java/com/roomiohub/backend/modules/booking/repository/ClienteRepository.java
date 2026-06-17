package com.roomiohub.backend.modules.booking.repository;

import com.roomiohub.backend.modules.booking.model.ClienteEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends BaseJpaRepository<ClienteEntity, UUID>, JpaSpecificationExecutor<ClienteEntity> {
    
    Optional<ClienteEntity> findByNumeroDocumentoAndTenantId(String numeroDocumento, UUID tenantId);
}
