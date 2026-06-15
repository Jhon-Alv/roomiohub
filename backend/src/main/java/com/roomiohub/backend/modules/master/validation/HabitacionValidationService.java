package com.roomiohub.backend.modules.master.validation;

import com.roomiohub.backend.modules.master.repository.HabitacionRepository;
import com.roomiohub.backend.security.identity.model.CuentaEntity;
import com.roomiohub.backend.security.identity.repository.CuentaRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class HabitacionValidationService {

    private final CuentaRepository cuentaRepository;
    private final HabitacionRepository habitacionRepository;

    public void validate(UUID tenantId) {
        CuentaEntity cuenta = cuentaRepository.findById(tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

        Integer maxHabitaciones = cuenta.getPlan().getMaxHabitaciones();
        
        // -1 significa ilimitado
        if (maxHabitaciones != null && maxHabitaciones != -1) {
            long habitacionesActuales = habitacionRepository.countByTenantId(tenantId);
            if (habitacionesActuales >= maxHabitaciones) {
                throw new RestException(HttpStatus.UNPROCESSABLE_CONTENT,
                        "Has alcanzado el límite de habitaciones (" + maxHabitaciones + ") para tu plan actual. Mejora tu plan para crear más.");
            }
        }
    }
}
