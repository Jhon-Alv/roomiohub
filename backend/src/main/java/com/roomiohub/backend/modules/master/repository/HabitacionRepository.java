package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HabitacionRepository extends BaseJpaRepository<HabitacionEntity, UUID>, JpaSpecificationExecutor<HabitacionEntity> {

    Optional<HabitacionEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    List<HabitacionEntity> findByTenantIdAndSedeIdAndActivoTrue(UUID tenantId, UUID sedeId);

    boolean existsByNumeroAndSedeId(String numero, UUID sedeId);

    long countByTenantId(UUID tenantId);
}
