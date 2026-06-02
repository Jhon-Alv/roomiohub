package com.roomiohub.backend.security.identity.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cuentas")
public class CuentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nombre_comercial", nullable = false)
    private String nombreComercial;

    @Column(name = "ruc", nullable = false, unique = true, length = 20)
    private String ruc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanEntity plan;

    @Builder.Default
    @Column(name = "fecha_inicio_suscripcion", nullable = false, updatable = false)
    private LocalDateTime fechaInicioSuscripcion = LocalDateTime.now();

    @Column(name = "fecha_fin_suscripcion", nullable = false)
    private LocalDateTime fechaFinSuscripcion;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_suscripcion", nullable = false, length = 50)
    private EstadoSuscripcion estadoSuscripcion = EstadoSuscripcion.ACTIVO;
}
