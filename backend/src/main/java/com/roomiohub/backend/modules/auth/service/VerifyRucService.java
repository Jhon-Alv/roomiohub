package com.roomiohub.backend.modules.auth.service;

import com.roomiohub.backend.modules.auth.dto.response.VerifyRucResponse;
import com.roomiohub.backend.security.identity.model.CuentaEntity;
import com.roomiohub.backend.security.identity.repository.CuentaRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyRucService {

    private final CuentaRepository cuentaRepository;

    public VerifyRucResponse execute(String ruc) {
        log.info("Verificando RUC: {}", ruc);
        
        CuentaEntity cuenta = cuentaRepository.findByRuc(ruc)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "No existe ninguna empresa registrada con este RUC"));

        return new VerifyRucResponse(cuenta.getNombreComercial());
    }
}
