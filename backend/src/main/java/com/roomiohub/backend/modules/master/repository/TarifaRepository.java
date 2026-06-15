package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.TarifaEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TarifaRepository extends BaseJpaRepository<TarifaEntity, UUID>, JpaSpecificationExecutor<TarifaEntity> {

    Optional<TarifaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    
    List<TarifaEntity> findAllByIdInAndTenantId(List<UUID> ids, UUID tenantId);

    boolean existsByCategoriaIdAndPlanTiempoIdAndTenantId(UUID categoriaId, UUID planTiempoId, UUID tenantId);
}
