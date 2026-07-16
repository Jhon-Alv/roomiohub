package com.roomiohub.backend.modules.metrics.dto.dashboard.response;

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
public class DashboardResponse {
    private BigDecimal ingresosHoy;
    private double tasaOcupacion;
    private int habitacionesSucias;
    private double promedioLimpiezaMinutos;
    private int habitacionesTotales;
    private int habitacionesOcupadas;
    private List<DashboardEstadoResumen> estadoHabitaciones;
    private BigDecimal ingresosUltimos3Dias;
    private BigDecimal ingresosUltimos7Dias;
    private BigDecimal ingresosUltimos30Dias;
}
