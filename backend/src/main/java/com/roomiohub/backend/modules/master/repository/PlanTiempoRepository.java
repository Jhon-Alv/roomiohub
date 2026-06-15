package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.PlanTiempoEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanTiempoRepository extends BaseJpaRepository<PlanTiempoEntity, UUID>, JpaSpecificationExecutor<PlanTiempoEntity> {

    Optional<PlanTiempoEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    boolean existsByNombreAndTenantId(String nombre, UUID tenantId);
}
