package com.roomiohub.backend.modules.admin.repository;

import com.roomiohub.backend.modules.admin.model.SedeEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SedeRepository extends BaseJpaRepository<SedeEntity, UUID> {
    
    @Query("SELECT s FROM SedeEntity s")
    List<SedeEntity> findAllSedes();

    @Query("SELECT s FROM SedeEntity s WHERE s.id = :id")
    Optional<SedeEntity> findById(@Param("id") UUID id);

    @Query("SELECT COUNT(s) FROM SedeEntity s")
    long countSedes();
}
