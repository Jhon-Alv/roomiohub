package com.roomiohub.backend.modules.master.api.metrics;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IMetricsHabitacionPort {

    List<HabitacionDataDto> getCatalogoHabitaciones(UUID sedeId);

    Map<EstadoHabitacion, Long> getEstadoHabitaciones(UUID sedeId);

    Double getPromedioLimpiezaMinutos(UUID sedeId);
}
