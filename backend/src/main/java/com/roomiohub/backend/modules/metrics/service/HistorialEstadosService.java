package com.roomiohub.backend.modules.metrics.service;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.master.api.metrics.IMetricsHabitacionPort;
import com.roomiohub.backend.modules.master.model.LogEstadoHabitacionEntity;
import com.roomiohub.backend.modules.master.repository.LogEstadoHabitacionRepository;
import com.roomiohub.backend.modules.metrics.dto.historial.response.HistorialEstadoResponse;
import com.roomiohub.backend.modules.metrics.mapper.HistorialEstadoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistorialEstadosService {

    private final LogEstadoHabitacionRepository logEstadoHabitacionRepository;
    private final IMetricsHabitacionPort metricsHabitacionPort;
    private final HistorialEstadoMapper historialEstadoMapper;

    @Transactional(readOnly = true)
    public Page<HistorialEstadoResponse> execute(UUID sedeId, UUID habitacionId, Pageable pageable) {
        Page<LogEstadoHabitacionEntity> page = logEstadoHabitacionRepository
                .findHistorialPaginado(sedeId, habitacionId, pageable);

        List<HabitacionDataDto> habitaciones = metricsHabitacionPort.getCatalogoHabitaciones(sedeId);
        Map<UUID, HabitacionDataDto> habitacionMap = habitaciones.stream()
                .collect(Collectors.toMap(HabitacionDataDto::getId, h -> h));

        List<HistorialEstadoResponse> items = page.getContent().stream()
                .map(log -> {
                    HistorialEstadoResponse response = historialEstadoMapper.toResponse(log);
                    HabitacionDataDto hab = habitacionMap.get(log.getHabitacionId());
                    if (hab != null) {
                        response.setHabitacionNumero(hab.getNumero());
                        response.setHabitacionCategoria(hab.getCategoriaNombre());
                    }
                    return response;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(items, pageable, page.getTotalElements());
    }
}
