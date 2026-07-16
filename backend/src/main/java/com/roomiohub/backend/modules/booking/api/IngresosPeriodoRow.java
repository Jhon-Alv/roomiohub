package com.roomiohub.backend.modules.booking.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngresosPeriodoRow {
    private BigDecimal hoy;
    private BigDecimal ultimos3Dias;
    private BigDecimal ultimos7Dias;
    private BigDecimal ultimos30Dias;
}
