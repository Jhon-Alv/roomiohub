package com.roomiohub.backend.modules.booking.dto.response;

import com.roomiohub.backend.modules.booking.model.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutResponse {

    private UUID id;
    private UUID habitacionId;
    private UUID clienteId;
    private Integer cantidadHuespedes;
    private MetodoPago metodoPago;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalidaEsperada;
    private LocalDateTime fechaSalidaReal;
    private BigDecimal montoTotal;
    
}
