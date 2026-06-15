package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.PromocionEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromocionRepository extends BaseJpaRepository<PromocionEntity, UUID>, JpaSpecificationExecutor<PromocionEntity> {

    Optional<PromocionEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    boolean existsByNombreAndTenantId(String nombre, UUID tenantId);
    
    boolean existsByIdAndTenantId(UUID id, UUID tenantId);
}
