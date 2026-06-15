package com.roomiohub.backend.modules.master.service.promocion;

import com.roomiohub.backend.modules.master.dto.promocion.request.ChangePromocionStatusRequest;
import com.roomiohub.backend.modules.master.model.PromocionEntity;
import com.roomiohub.backend.modules.master.repository.PromocionRepository;
import com.roomiohub.backend.modules.master.validation.PromocionValidationService;
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
public class ChangePromocionStatusService {

    private final PromocionRepository promocionRepository;
    private final PromocionValidationService validationService;

    @Transactional
    public void execute(UUID promocionId, ChangePromocionStatusRequest request) {
        log.info("Cambiando estado de la promoción con ID: {}", promocionId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        validationService.validatePlanAccess(tenantId);

        PromocionEntity promocion = promocionRepository.findByIdAndTenantId(promocionId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Promoción no existe o no tiene acceso"));

        if (Objects.equals(promocion.getActivo(), request.getActivo())) {
            log.warn("La promoción con ID: {} ya se encuentra en estado: {}", promocionId, request.getActivo());
            return;
        }

        promocion.setActivo(request.getActivo());
        promocionRepository.merge(promocion);

        log.info("Estado de la promoción con ID: {} cambiado a: {}", promocionId, request.getActivo());
    }
}
