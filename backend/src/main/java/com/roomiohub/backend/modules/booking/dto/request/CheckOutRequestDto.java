package com.roomiohub.backend.modules.booking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutRequestDto {

    @NotNull(message = "El ID de la sede es obligatorio")
    private UUID sedeId;

    @NotNull(message = "El monto de penalidad no puede ser nulo, enviar 0 si no hay")
    private BigDecimal montoPenalidad;

    private String observacion;
}
