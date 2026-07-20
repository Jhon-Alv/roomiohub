package com.roomiohub.backend.modules.booking.repository;

import com.roomiohub.backend.modules.booking.api.IngresosPeriodoRow;
import com.roomiohub.backend.modules.booking.model.ReservaEntity;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservaRepository extends BaseJpaRepository<ReservaEntity, UUID>, JpaSpecificationExecutor<ReservaEntity> {

    Optional<ReservaEntity> findByHabitacionIdAndFechaSalidaRealIsNull(UUID habitacionId);

    @Query("SELECT new com.roomiohub.backend.modules.booking.api.IngresosPeriodoRow(" +
           "  COALESCE(SUM(CASE WHEN r.fechaIngreso >= :hoy THEN r.montoTotal ELSE 0 END), 0), " +
           "  COALESCE(SUM(CASE WHEN r.fechaIngreso >= :d3 THEN r.montoTotal ELSE 0 END), 0), " +
           "  COALESCE(SUM(CASE WHEN r.fechaIngreso >= :d7 THEN r.montoTotal ELSE 0 END), 0), " +
           "  COALESCE(SUM(CASE WHEN r.fechaIngreso >= :d30 THEN r.montoTotal ELSE 0 END), 0)" +
           ") FROM ReservaEntity r WHERE r.sedeId = :sedeId AND r.fechaIngreso >= :d30")
    List<IngresosPeriodoRow> sumIngresosPeriodos(@Param("sedeId") UUID sedeId,
                                                  @Param("hoy") LocalDateTime hoy,
                                                  @Param("d3") LocalDateTime d3,
                                                  @Param("d7") LocalDateTime d7,
                                                  @Param("d30") LocalDateTime d30);

    @Query(value = "SELECT TO_CHAR(DATE(r.fecha_ingreso), 'YYYY-MM-DD'), " +
           "COALESCE(SUM(r.monto_total), 0) " +
           "FROM reservas r " +
           "WHERE r.sede_id = :sedeId AND r.fecha_ingreso >= :desde " +
           "GROUP BY DATE(r.fecha_ingreso) ORDER BY DATE(r.fecha_ingreso) ASC",
           nativeQuery = true)
    List<Object[]> sumIngresosAgrupadosPorDia(@Param("sedeId") UUID sedeId,
                                               @Param("desde") LocalDateTime desde);
}
