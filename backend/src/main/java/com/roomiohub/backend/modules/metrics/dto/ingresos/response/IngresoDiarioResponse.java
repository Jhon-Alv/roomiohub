package com.roomiohub.backend.modules.metrics.dto.ingresos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngresoDiarioResponse {
    private LocalDate fecha;
    private BigDecimal monto;
}
