package com.roomiohub.backend.modules.booking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutRequest {

    @NotNull(message = "El ID de la sede es obligatorio")
    private UUID sedeId;

    @PositiveOrZero(message = "El monto total debe ser 0 o mayor")
    private BigDecimal montoPenalidad;
}
