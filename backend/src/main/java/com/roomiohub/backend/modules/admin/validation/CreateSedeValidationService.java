package com.roomiohub.backend.modules.admin.validation;

import com.roomiohub.backend.security.identity.model.SedeEntity;
import com.roomiohub.backend.security.identity.repository.SedeRepository;
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
public class CreateSedeValidationService {

    private final CuentaRepository cuentaRepository;
    private final SedeRepository sedeRepository;

    public void validate(SedeEntity sede) {

        CuentaEntity cuenta = cuentaRepository.findById(sede.getTenantId())
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

        Integer maxSedesPermitidas = cuenta.getPlan().getMaxSedes();

        long sedesActuales = sedeRepository.countSedes();

        if (sedesActuales >= maxSedesPermitidas) {
            log.warn("Límite de sedes alcanzado para el tenant {}. Máximo: {}", sede.getTenantId(), maxSedesPermitidas);
            throw new RestException(HttpStatus.UNPROCESSABLE_CONTENT,
                    "Has alcanzado el límite de sedes (" + maxSedesPermitidas + ") para tu plan actual. Mejora tu plan para crear más.");
        }

    }
}