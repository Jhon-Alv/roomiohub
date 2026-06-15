package com.roomiohub.backend.modules.master.service.promociontarifa;

import com.roomiohub.backend.modules.master.repository.PromocionTarifaRepository;
import com.roomiohub.backend.modules.master.model.PromocionTarifaEntity;
import com.roomiohub.backend.modules.master.validation.PromocionValidationService;
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
public class ChangePromocionTarifaStatusService {

    private final PromocionTarifaRepository repository;
    private final PromocionValidationService validationService;

    @Transactional
    public void execute(UUID promocionId, UUID tarifaAsociacionId, boolean activo) {
        log.info("Cambiando estado activo={} para asociación {} en promoción {}", activo, tarifaAsociacionId, promocionId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        validationService.validatePlanAccess(tenantId);

        PromocionTarifaEntity pt = repository.findByPromocionIdAndId(promocionId, tarifaAsociacionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Asociación no encontrada"));

        if (!pt.getPromocion().getTenantId().equals(tenantId)) {
            throw new RestException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        pt.setActivo(activo);
        repository.merge(pt);
    }
}
