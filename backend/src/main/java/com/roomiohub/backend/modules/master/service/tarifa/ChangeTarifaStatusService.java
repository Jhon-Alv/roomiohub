package com.roomiohub.backend.modules.master.service.tarifa;

import com.roomiohub.backend.modules.master.dto.tarifa.request.ChangeTarifaStatusRequest;
import com.roomiohub.backend.modules.master.model.TarifaEntity;
import com.roomiohub.backend.modules.master.repository.TarifaRepository;
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
public class ChangeTarifaStatusService {

    private final TarifaRepository tarifaRepository;

    @Transactional
    public void execute(UUID tarifaId, ChangeTarifaStatusRequest request) {
        log.info("Cambiando estado de la tarifa con ID: {}", tarifaId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        TarifaEntity tarifa = tarifaRepository.findByIdAndTenantId(tarifaId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Tarifa no existe o no tiene acceso"));

        if (Objects.equals(tarifa.getActivo(), request.getActivo())) {
            log.warn("La tarifa con ID: {} ya se encuentra en estado: {}", tarifaId, request.getActivo());
            return;
        }

        tarifa.setActivo(request.getActivo());
        tarifaRepository.merge(tarifa);

        log.info("Estado de la tarifa con ID: {} cambiado a: {}", tarifaId, request.getActivo());
    }
}
