package com.roomiohub.backend.modules.master.dto.tarifa.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateTarifaRequest {

    @NotNull(message = "La categoria es obligatoria")
    private UUID categoriaId;

    @NotNull(message = "El plan de tiempo es obligatorio")
    private UUID planTiempoId;

    @NotNull(message = "El precio base es obligatorio")
    @PositiveOrZero(message = "El precio debe ser 0 o mayor")
    private BigDecimal precioBase;
}
