package com.roomiohub.backend.security.identity.repository;

import com.roomiohub.backend.security.identity.model.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, UUID> {

  List<PlanEntity> findAllByActivoTrue();

}
