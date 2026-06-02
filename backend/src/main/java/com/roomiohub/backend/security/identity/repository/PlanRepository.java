package com.roomiohub.backend.security.identity.repository;

import com.roomiohub.backend.security.identity.model.PlanEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlanRepository extends BaseJpaRepository<PlanEntity, UUID> {
}
