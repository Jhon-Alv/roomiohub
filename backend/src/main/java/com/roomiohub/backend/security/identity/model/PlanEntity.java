package com.roomiohub.backend.security.identity.model;

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
@Table(name = "planes")
public class PlanEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "nombre", nullable = false, unique = true, length = 50)
  private String nombre;

  @Column(name = "max_sedes", nullable = false)
  private Integer maxSedes;

  @Column(name = "max_habitaciones", nullable = false)
  private Integer maxHabitaciones;

  @Column(name = "max_usuarios", nullable = false)
  private Integer maxUsuarios;

  @Column(name = "flag_modulo_promociones", nullable = false)
  private Boolean flagModuloPromociones;

  @Column(name = "precio_mensual", nullable = false, precision = 10, scale = 2)
  private BigDecimal precioMensual;

  @Column(name = "activo", insertable = false)
  private Boolean activo;

}
