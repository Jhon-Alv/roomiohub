package com.roomiohub.backend.modules.master.service.promocion;

import com.roomiohub.backend.modules.master.dto.promocion.response.FindPromocionesResponse;
import com.roomiohub.backend.modules.master.mapper.PromocionMapper;
import com.roomiohub.backend.modules.master.model.PromocionEntity;
import com.roomiohub.backend.modules.master.repository.PromocionRepository;
import com.roomiohub.backend.modules.master.repository.PromocionSpecification;
import com.roomiohub.backend.modules.master.validation.PromocionValidationService;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindPromocionesService {

    private final PromocionRepository promocionRepository;
    private final PromocionMapper promocionMapper;
    private final PromocionValidationService validationService;

    @Transactional(readOnly = true)
    public List<FindPromocionesResponse> execute(String search, Boolean activo) {
        log.info("Obteniendo lista de promociones con filtros: search={}, activo={}", search, activo);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();
        validationService.validatePlanAccess(tenantId);

        Specification<PromocionEntity> spec = PromocionSpecification.getFiltros(search, activo);

        List<PromocionEntity> promociones = promocionRepository.findAll(spec);

        return promocionMapper.toFindListResponse(promociones);
    }
}
