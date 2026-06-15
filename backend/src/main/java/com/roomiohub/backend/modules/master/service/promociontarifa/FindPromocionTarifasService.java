package com.roomiohub.backend.modules.master.service.promociontarifa;

import com.roomiohub.backend.modules.master.dto.promociontarifa.response.PromocionTarifaResponse;
import com.roomiohub.backend.modules.master.mapper.PromocionTarifaMapper;
import com.roomiohub.backend.modules.master.model.PromocionTarifaEntity;
import com.roomiohub.backend.modules.master.repository.PromocionRepository;
import com.roomiohub.backend.modules.master.repository.PromocionTarifaRepository;
import com.roomiohub.backend.modules.master.validation.PromocionValidationService;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindPromocionTarifasService {

    private final PromocionTarifaRepository promocionTarifaRepository;
    private final PromocionRepository promocionRepository;
    private final PromocionTarifaMapper mapper;
    private final PromocionValidationService validationService;

    @Transactional(readOnly = true)
    public List<PromocionTarifaResponse> execute(UUID promocionId) {
        log.info("Obteniendo tarifas asociadas a la promoción: {}", promocionId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        validationService.validatePlanAccess(tenantId);

        if (!promocionRepository.existsByIdAndTenantId(promocionId, tenantId)) {
             throw new RestException(HttpStatus.NOT_FOUND, "Promoción no encontrada");
        }

        List<PromocionTarifaEntity> entidades = promocionTarifaRepository.findByPromocionId(promocionId);
        return mapper.toListResponse(entidades);
    }
}
