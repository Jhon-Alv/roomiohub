package com.roomiohub.backend.modules.master.service.promocion;

import com.roomiohub.backend.modules.master.dto.promocion.request.CreatePromocionRequest;
import com.roomiohub.backend.modules.master.dto.promocion.response.CreatePromocionResponse;
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
public class CreatePromocionService {

    private final PromocionRepository promocionRepository;
    private final PromocionMapper promocionMapper;
    private final PromocionValidationService validationService;

    @Transactional
    public CreatePromocionResponse execute(CreatePromocionRequest request) {
        log.info("Iniciando proceso de creación de promoción con nombre: {}", request.getNombre());

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        
        validationService.validatePlanAccess(tenantId);

        if (promocionRepository.existsByNombreAndTenantId(request.getNombre(), tenantId)) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una promoción con el nombre especificado");
        }

        PromocionEntity promocion = promocionMapper.toCreateEntity(request);
        promocion.setTenantId(tenantId);

        PromocionEntity savedPromocion = promocionRepository.persist(promocion);
        log.info("Creación de promoción con ID: {}", savedPromocion.getId());

        return promocionMapper.toCreateResponse(savedPromocion);
    }
}
