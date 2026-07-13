package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.LogEstadoHabitacionEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LogEstadoHabitacionRepository extends BaseJpaRepository<LogEstadoHabitacionEntity, UUID> {

    @Query("SELECT l FROM LogEstadoHabitacionEntity l " +
           "WHERE l.habitacionId = :habitacionId " +
           "ORDER BY l.fechaCambio DESC")
    List<LogEstadoHabitacionEntity> findByHabitacionIdOrderByFechaCambioDesc(@Param("habitacionId") UUID habitacionId);

    Optional<LogEstadoHabitacionEntity> findFirstByHabitacionIdOrderByFechaCambioDesc(UUID habitacionId);

    @Query("SELECT l FROM LogEstadoHabitacionEntity l " +
           "WHERE l.sedeId = :sedeId AND l.habitacionId IN :habitacionIds " +
           "AND l.id IN (SELECT MAX(l2.id) FROM LogEstadoHabitacionEntity l2 " +
           "             WHERE l2.habitacionId = l.habitacionId GROUP BY l2.habitacionId)")
    List<LogEstadoHabitacionEntity> findUltimoCambioByHabitacionIds(@Param("sedeId") UUID sedeId,
                                                                     @Param("habitacionIds") List<UUID> habitacionIds);
}
