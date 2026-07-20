package com.roomiohub.backend.modules.metrics.service;

import com.roomiohub.backend.modules.master.api.metrics.IMetricsHabitacionPort;
import com.roomiohub.backend.modules.metrics.dto.habitacion.response.HabitacionCatalogoItem;
import com.roomiohub.backend.modules.metrics.mapper.HabitacionCatalogoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HabitacionCatalogoService {

    private final IMetricsHabitacionPort metricsHabitacionPort;
    private final HabitacionCatalogoMapper habitacionCatalogoMapper;

    @Transactional(readOnly = true)
    public List<HabitacionCatalogoItem> execute(UUID sedeId) {
        return habitacionCatalogoMapper.toItemList(metricsHabitacionPort.getCatalogoHabitaciones(sedeId));
    }
}
