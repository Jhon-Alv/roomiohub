package com.roomiohub.backend.modules.master.model;

import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import com.roomiohub.backend.shared.persistence.TenantEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "log_estados_habitacion")
public class LogEstadoHabitacionEntity extends TenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sede_id", nullable = false)
    private UUID sedeId;

    @Column(name = "habitacion_id", nullable = false)
    private UUID habitacionId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado_anterior", nullable = false)
    private EstadoHabitacion estadoAnterior;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado_nuevo", nullable = false)
    private EstadoHabitacion estadoNuevo;

    @Builder.Default
    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio = LocalDateTime.now();

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;
}
