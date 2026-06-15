package com.roomiohub.backend.modules.master.service.habitacion;

import com.roomiohub.backend.modules.master.dto.habitacion.response.FindHabitacionResponse;
import com.roomiohub.backend.modules.master.mapper.HabitacionMapper;
import com.roomiohub.backend.modules.master.model.HabitacionEntity;
import com.roomiohub.backend.modules.master.repository.HabitacionRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindHabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    @Transactional(readOnly = true)
    public FindHabitacionResponse execute(UUID habitacionId){
        log.info("Obteniendo habitación con ID: {}", habitacionId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        HabitacionEntity habitacion = habitacionRepository.findByIdAndTenantId(habitacionId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Habitación no encontrada con ID: " + habitacionId));

        log.info("Habitación encontrada: {} - {}", habitacion.getId(), habitacion.getNumero());

        return habitacionMapper.toFindResponse(habitacion);
    }
}
