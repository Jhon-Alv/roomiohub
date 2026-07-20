package com.roomiohub.backend.modules.master.repository;

import com.roomiohub.backend.modules.master.model.LogEstadoHabitacionEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
           "AND l.fechaCambio IN (SELECT MAX(l2.fechaCambio) FROM LogEstadoHabitacionEntity l2 " +
           "                     WHERE l2.habitacionId = l.habitacionId GROUP BY l2.habitacionId)")
    List<LogEstadoHabitacionEntity> findUltimoCambioByHabitacionIds(@Param("sedeId") UUID sedeId,
                                                                     @Param("habitacionIds") List<UUID> habitacionIds);

    @Query(value = "SELECT COALESCE(AVG(EXTRACT(EPOCH FROM (d.fecha_cambio - s.fecha_cambio)) / 60), 0) " +
           "FROM log_estados_habitacion s " +
           "JOIN log_estados_habitacion d ON d.habitacion_id = s.habitacion_id " +
           "AND d.fecha_cambio = (SELECT MIN(d2.fecha_cambio) FROM log_estados_habitacion d2 " +
           "                      WHERE d2.habitacion_id = s.habitacion_id " +
           "                      AND d2.estado_nuevo = 'DISPONIBLE' AND d2.fecha_cambio > s.fecha_cambio) " +
           "WHERE s.sede_id = :sedeId AND s.estado_nuevo = 'SUCIO' " +
           "AND d.estado_nuevo = 'DISPONIBLE'", nativeQuery = true)
    Double avgLimpiezaMinutos(@Param("sedeId") UUID sedeId);

    @Query("SELECT l FROM LogEstadoHabitacionEntity l " +
           "WHERE l.sedeId = :sedeId " +
           "AND (:habitacionId IS NULL OR l.habitacionId = :habitacionId) " +
           "ORDER BY l.fechaCambio DESC")
    Page<LogEstadoHabitacionEntity> findHistorialPaginado(@Param("sedeId") UUID sedeId,
                                                           @Param("habitacionId") UUID habitacionId,
                                                           Pageable pageable);
}
