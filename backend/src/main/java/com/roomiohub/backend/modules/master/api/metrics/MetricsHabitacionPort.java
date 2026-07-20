package com.roomiohub.backend.modules.master.api.metrics;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.master.mapper.HabitacionMapper;
import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import com.roomiohub.backend.modules.master.repository.HabitacionRepository;
import com.roomiohub.backend.modules.master.repository.LogEstadoHabitacionRepository;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricsHabitacionPort implements IMetricsHabitacionPort {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;
    private final LogEstadoHabitacionRepository logEstadoHabitacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionDataDto> getCatalogoHabitaciones(UUID sedeId) {
        List<HabitacionEntity> habitaciones = habitacionRepository.findByTenantIdAndSedeIdAndActivoTrue(
                com.roomiohub.backend.security.tenant.TenantContextHolder.getRequiredTenantId(), sedeId);
        return habitacionMapper.toDataDtoList(habitaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<EstadoHabitacion, Long> getEstadoHabitaciones(UUID sedeId) {
        return getCatalogoHabitaciones(sedeId).stream()
                .collect(Collectors.groupingBy(HabitacionDataDto::getEstadoActual, Collectors.counting()));
    }

    @Override
    @Transactional(readOnly = true)
    public Double getPromedioLimpiezaMinutos(UUID sedeId) {
        return logEstadoHabitacionRepository.avgLimpiezaMinutos(sedeId);
    }
}
