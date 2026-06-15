package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.PisoEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PisoRepository extends BaseJpaRepository<PisoEntity, UUID>, JpaSpecificationExecutor<PisoEntity> {

    Optional<PisoEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    boolean existsByNombreAndSedeId(String nombre, UUID sedeId);
}
