package com.roomiohub.backend.modules.master.dto.promociontarifa.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PromocionTarifaItemRequest {

    @NotNull(message = "La tarifa es obligatoria")
    private UUID tarifaId;

    @NotNull(message = "El precio de promoción es obligatorio")
    @PositiveOrZero(message = "El precio de promoción debe ser 0 o mayor")
    private BigDecimal precioPromocion;
}
