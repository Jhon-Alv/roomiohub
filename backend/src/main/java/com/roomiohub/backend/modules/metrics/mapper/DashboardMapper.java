package com.roomiohub.backend.modules.metrics.mapper;

import com.roomiohub.backend.modules.metrics.dto.dashboard.response.DashboardEstadoResumen;
import com.roomiohub.backend.modules.metrics.dto.dashboard.response.DashboardResponse;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DashboardMapper {

    default DashboardResponse toResponse(BigDecimal ingresosHoy, double tasaOcupacion, int habitacionesSucias,
                                          double promedioLimpiezaMinutos, int total, int ocupadas,
                                          Map<EstadoHabitacion, Long> estadoHabitaciones,
                                          BigDecimal ingresos3d, BigDecimal ingresos7d, BigDecimal ingresos30d) {
        return DashboardResponse.builder()
                .ingresosHoy(ingresosHoy)
                .tasaOcupacion(tasaOcupacion)
                .habitacionesSucias(habitacionesSucias)
                .promedioLimpiezaMinutos(promedioLimpiezaMinutos)
                .habitacionesTotales(total)
                .habitacionesOcupadas(ocupadas)
                .estadoHabitaciones(estadoHabitaciones.entrySet().stream()
                        .filter(e -> e.getValue() > 0)
                        .map(e -> DashboardEstadoResumen.builder()
                                .estado(e.getKey()).cantidad(e.getValue()).build())
                        .collect(Collectors.toList()))
                .ingresosUltimos3Dias(ingresos3d)
                .ingresosUltimos7Dias(ingresos7d)
                .ingresosUltimos30Dias(ingresos30d)
                .build();
    }
}
