package com.roomiohub.backend.modules.booking.api;

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
    private List<IngresoDiario> dias;
    private BigDecimal total;
}
