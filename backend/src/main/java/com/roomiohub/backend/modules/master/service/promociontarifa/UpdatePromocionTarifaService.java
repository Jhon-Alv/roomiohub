package com.roomiohub.backend.modules.master.service.promociontarifa;

import com.roomiohub.backend.modules.master.dto.promociontarifa.request.UpdatePromocionTarifaPriceRequest;
import com.roomiohub.backend.modules.master.model.PromocionTarifaEntity;
import com.roomiohub.backend.modules.master.repository.PromocionTarifaRepository;
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
public class UpdatePromocionTarifaService {

    private final PromocionTarifaRepository repository;
    private final PromocionValidationService validationService;

    @Transactional
    public void execute(UUID promocionId, UUID tarifaAsociacionId, UpdatePromocionTarifaPriceRequest request) {
        log.info("Actualizando precio para la asociación tarifa {} en promoción {}", tarifaAsociacionId, promocionId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        validationService.validatePlanAccess(tenantId);

        PromocionTarifaEntity promocionTarifat = repository.findByPromocionIdAndId(promocionId, tarifaAsociacionId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Asociación no encontrada"));

        if (!promocionTarifat.getPromocion().getTenantId().equals(tenantId)) {
            throw new RestException(HttpStatus.FORBIDDEN, "Acceso denegado");
        }

        promocionTarifat.setPrecioPromocion(request.getPrecioPromocion());
        repository.merge(promocionTarifat);
        log.info("Precio de asociación actualizado exitosamente");
    }
}
