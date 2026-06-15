package com.roomiohub.backend.modules.master.service.promocion;

import com.roomiohub.backend.modules.master.dto.promocion.response.FindPromocionResponse;
import com.roomiohub.backend.modules.master.mapper.PromocionMapper;
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

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindPromocionService {

    private final PromocionRepository promocionRepository;
    private final PromocionMapper promocionMapper;
    private final PromocionValidationService validationService;

    @Transactional(readOnly = true)
    public FindPromocionResponse execute(UUID promocionId){
        log.info("Obteniendo promoción con ID: {}", promocionId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        validationService.validatePlanAccess(tenantId);

        PromocionEntity promocion = promocionRepository.findByIdAndTenantId(promocionId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Promoción no encontrada con ID: " + promocionId));

        log.info("Promoción encontrada: {} - {}", promocion.getId(), promocion.getNombre());

        return promocionMapper.toFindResponse(promocion);
    }
}
