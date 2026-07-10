package com.roomiohub.backend.modules.booking.repository;

import com.roomiohub.backend.modules.booking.model.ReservaEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservaRepository extends BaseJpaRepository<ReservaEntity, UUID>, JpaSpecificationExecutor<ReservaEntity> {

    Optional<ReservaEntity> findByHabitacionIdAndFechaSalidaRealIsNull(UUID habitacionId);
}
