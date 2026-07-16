package com.roomiohub.backend.modules.metrics.service;

import com.roomiohub.backend.modules.booking.api.IMetricsReservaPort;
import com.roomiohub.backend.modules.booking.api.IngresosResponse;
import com.roomiohub.backend.modules.master.api.metrics.IMetricsHabitacionPort;
import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.metrics.dto.dashboard.response.DashboardResponse;
import com.roomiohub.backend.modules.metrics.mapper.DashboardMapper;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IMetricsReservaPort metricsReservaPort;
    private final IMetricsHabitacionPort metricsHabitacionPort;
    private final DashboardMapper dashboardMapper;

    @Transactional(readOnly = true)
    public DashboardResponse execute(UUID sedeId) {

        List<HabitacionDataDto> habitaciones = metricsHabitacionPort.getCatalogoHabitaciones(sedeId);
        int total = habitaciones.size();
        int ocupadas = (int) habitaciones.stream().filter(h -> h.getEstadoActual() == EstadoHabitacion.OCUPADO).count();
        int sucias = (int) habitaciones.stream().filter(h -> h.getEstadoActual() == EstadoHabitacion.SUCIO).count();
        double tasaOcupacion = total > 0 ? (ocupadas * 100.0 / total) : 0;

        Map<EstadoHabitacion, Long> estadoHabitaciones = metricsHabitacionPort.getEstadoHabitaciones(sedeId);

        Double promedio = metricsHabitacionPort.getPromedioLimpiezaMinutos(sedeId);
        double promedioLimpiezaMinutos = promedio != null ? promedio : 0;

        IngresosResponse ingresos = metricsReservaPort.sumIngresos(sedeId);

        return dashboardMapper.toResponse(ingresos.getHoy(), tasaOcupacion, sucias,
                promedioLimpiezaMinutos, total, ocupadas, estadoHabitaciones,
                ingresos.getUltimos3Dias(), ingresos.getUltimos7Dias(), ingresos.getUltimos30Dias());
    }
}
