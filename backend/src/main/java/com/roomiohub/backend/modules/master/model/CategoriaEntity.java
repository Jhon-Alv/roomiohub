package com.roomiohub.backend.modules.master.model;

import com.roomiohub.backend.shared.persistence.TenantBaseEntity;
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
@Table(name = "categorias")
public class CategoriaEntity extends TenantBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "precio_hora_extra", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal precioHoraExtra = BigDecimal.ZERO;

}
