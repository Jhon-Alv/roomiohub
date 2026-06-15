package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends BaseJpaRepository<CategoriaEntity, UUID>, JpaSpecificationExecutor<CategoriaEntity> {

    Optional<CategoriaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<CategoriaEntity> findByNombreAndActivoTrue(String nombre);
    
    boolean existsByNombreAndTenantId(String nombre, UUID tenantId);
    
    List<CategoriaEntity> findAllByActivoTrue();
}
