package com.roomiohub.backend.modules.booking.dto.request;

import com.roomiohub.backend.modules.booking.model.MetodoPago;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInRequest {

    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombresCliente;

    @NotNull(message = "El ID de la sede es obligatorio")
    private UUID sedeId;

    @NotNull(message = "El ID de la tarifa es obligatorio")
    private UUID tarifaId;

    private UUID promocionId;

    @NotNull(message = "La cantidad de huéspedes es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 huésped")
    private Integer cantidadHuespedes;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;
}
