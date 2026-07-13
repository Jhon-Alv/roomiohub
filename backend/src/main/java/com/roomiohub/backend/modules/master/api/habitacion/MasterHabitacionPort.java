package com.roomiohub.backend.modules.master.api.habitacion;

import com.roomiohub.backend.modules.master.api.dto.HabitacionDataDto;
import com.roomiohub.backend.modules.master.mapper.HabitacionMapper;
import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import com.roomiohub.backend.modules.master.model.LogEstadoHabitacionEntity;
import com.roomiohub.backend.modules.master.repository.HabitacionRepository;
import com.roomiohub.backend.modules.master.repository.LogEstadoHabitacionRepository;
import com.roomiohub.backend.shared.enums.EstadoHabitacion;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MasterHabitacionPort implements IMasterHabitacionPort {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;
    private final LogEstadoHabitacionRepository logEstadoHabitacionRepository;

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
    public void updateEstadoHabitacion(UUID habitacionId, EstadoHabitacion nuevoEstado, UUID usuarioId, String observacion) {
        HabitacionEntity habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Habitación no encontrada"));

        EstadoHabitacion estadoAnterior = habitacion.getEstadoActual();
        habitacion.setEstadoActual(nuevoEstado);
        habitacionRepository.update(habitacion);

        LogEstadoHabitacionEntity log = LogEstadoHabitacionEntity.builder()
                .habitacionId(habitacionId)
                .sedeId(habitacion.getSede().getId())
                .usuarioId(usuarioId)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(nuevoEstado)
                .observacion(observacion)
                .build();
        logEstadoHabitacionRepository.persist(log);
    }
}
