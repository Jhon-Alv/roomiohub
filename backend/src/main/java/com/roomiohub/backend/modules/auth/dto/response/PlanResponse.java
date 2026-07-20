package com.roomiohub.backend.modules.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {

    private UUID id;
    private String nombre;
    private BigDecimal precioMensual;
    private Integer maxHabitaciones;
    private Integer maxUsuarios;
    private Integer maxSedes;
    private Boolean flagModuloPromociones;

}
