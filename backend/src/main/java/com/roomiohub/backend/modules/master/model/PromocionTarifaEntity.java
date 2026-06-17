package com.roomiohub.backend.modules.master.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "promocion_tarifas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"promocion_id", "tarifa_id"})
})
public class PromocionTarifaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promocion_id", nullable = false)
    private PromocionEntity promocion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarifa_id", nullable = false)
    private TarifaEntity tarifa;

    @Column(name = "precio_promocional", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPromocional;

    @Builder.Default
    @Column(name = "activo")
    private Boolean activo = true;
}
