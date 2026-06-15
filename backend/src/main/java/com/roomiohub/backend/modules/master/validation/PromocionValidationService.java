package com.roomiohub.backend.modules.master.validation;

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
public class PromocionValidationService {

    private final CuentaRepository cuentaRepository;

    public void validatePlanAccess(UUID tenantId) {
        CuentaEntity cuenta = cuentaRepository.findById(tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

        if (cuenta.getPlan() == null || !Boolean.TRUE.equals(cuenta.getPlan().getFlagModuloPromociones())) {
            throw new RestException(HttpStatus.FORBIDDEN, "Tu plan actual no incluye el módulo de promociones. Mejora tu plan para acceder a esta funcionalidad.");
        }
    }
}
