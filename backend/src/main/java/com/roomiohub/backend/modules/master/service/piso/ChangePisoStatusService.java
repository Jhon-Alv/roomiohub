package com.roomiohub.backend.modules.master.service.piso;

import com.roomiohub.backend.modules.master.dto.piso.request.ChangePisoStatusRequest;
import com.roomiohub.backend.modules.master.model.PisoEntity;
import com.roomiohub.backend.modules.master.repository.PisoRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangePisoStatusService {

    private final PisoRepository pisoRepository;

    @Transactional
    public void execute(UUID pisoId, ChangePisoStatusRequest request) {
        log.info("Cambiando estado del piso con ID: {}", pisoId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        PisoEntity piso = pisoRepository.findByIdAndTenantId(pisoId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Piso no existe o no tiene acceso"));

        if (Objects.equals(piso.getActivo(), request.getActivo())) {
            log.warn("El piso con ID: {} ya se encuentra en estado: {}", pisoId, request.getActivo());
            return;
        }

        piso.setActivo(request.getActivo());
        pisoRepository.merge(piso);

        log.info("Estado del piso con ID: {} cambiado a: {}", pisoId, request.getActivo());
    }
}
