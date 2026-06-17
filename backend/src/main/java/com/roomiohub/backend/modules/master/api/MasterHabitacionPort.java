package com.roomiohub.backend.modules.master.api;

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

import com.roomiohub.backend.modules.master.model.EstadoHabitacion;

@Service
@RequiredArgsConstructor
public class MasterHabitacionPort implements IMasterHabitacionPort {

    private final HabitacionRepository habitacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HabitacionDataDto> getHabitacionesActivasBySede(UUID tenantId, UUID sedeId) {
        List<HabitacionEntity> habitaciones = habitacionRepository.findByTenantIdAndSedeIdAndActivoTrue(tenantId, sedeId);
        return habitaciones.stream().map(h -> HabitacionDataDto.builder()
                .id(h.getId())
                .numero(h.getNumero())
                .categoriaNombre(h.getCategoria().getNombre())
                .pisoNombre(h.getPiso().getNombre())
                .estadoActual(h.getEstadoActual().name())
                .build()
        ).toList();
    }

    @Override
    @Transactional
    public void updateEstadoHabitacion(UUID habitacionId, String nuevoEstado) {
        HabitacionEntity habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Habitación no encontrada"));
        habitacion.setEstadoActual(EstadoHabitacion.valueOf(nuevoEstado));
        habitacionRepository.update(habitacion);
    }
}
