package com.roomiohub.backend.modules.master.service.promocion;

import com.roomiohub.backend.modules.master.dto.promocion.request.UpdatePromocionRequest;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePromocionService {

    private final PromocionRepository promocionRepository;
    private final PromocionMapper promocionMapper;
    private final PromocionValidationService validationService;

    @Transactional
    public void execute(UUID id, UpdatePromocionRequest request) {
        log.info("Iniciando proceso de actualización de promoción con ID: {}", id);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        validationService.validatePlanAccess(tenantId);

        PromocionEntity promocion = promocionRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Promoción no existe o no tiene acceso"));

        if (!promocion.getNombre().equals(request.getNombre()) && promocionRepository.existsByNombreAndTenantId(request.getNombre(), tenantId)) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una promoción con ese nombre");
        }

        promocionMapper.toUpdateEntity(promocion, request);

        promocionRepository.merge(promocion);

        log.info("Promoción con ID: {} actualizada exitosamente", id);
    }
}
