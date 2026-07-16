package com.roomiohub.backend.modules.metrics.dto.ingresos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngresosDiariosResponse {
    private List<IngresoDiarioResponse> dias;
    private BigDecimal total;
}
