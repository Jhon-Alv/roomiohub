package com.roomiohub.backend.modules.booking.dto.response;

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
public class FindReservaActivaResponse {

    private UUID id;
    private UUID habitacionId;
    private UUID clienteId;
    private UUID tarifaId;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalidaEsperada;
    private BigDecimal montoSubtotal;
    private BigDecimal montoDescuento;
    private BigDecimal montoPenalidad;
    private BigDecimal montoTotal;
    private BigDecimal precioHoraExtra;
    private Integer planTiempoHoras;
    private BigDecimal penalidadSugerida;
}
