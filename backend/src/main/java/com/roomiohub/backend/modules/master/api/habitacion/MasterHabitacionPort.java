package com.roomiohub.backend.modules.master.api.habitacion;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import com.roomiohub.backend.modules.master.repository.HabitacionRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import com.roomiohub.backend.shared.enums.EstadoHabitacion;

import com.roomiohub.backend.modules.master.mapper.HabitacionMapper;

@Service
@RequiredArgsConstructor
public class MasterHabitacionPort implements IMasterHabitacionPort {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionDataDto> getHabitacionesActivasBySede(UUID tenantId, UUID sedeId) {
        List<HabitacionEntity> habitaciones = habitacionRepository.findByTenantIdAndSedeIdAndActivoTrue(tenantId, sedeId);
        return habitacionMapper.toDataDtoList(habitaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public HabitacionDataDto getHabitacionById(UUID habitacionId) {
        HabitacionEntity habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Habitación no encontrada"));
        return habitacionMapper.toDataDto(habitacion);
    }

    @Override
    @Transactional
    public void updateEstadoHabitacion(UUID habitacionId, EstadoHabitacion nuevoEstado) {
        HabitacionEntity habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Habitación no encontrada"));
        habitacion.setEstadoActual(nuevoEstado);
        habitacionRepository.update(habitacion);
    }
}
