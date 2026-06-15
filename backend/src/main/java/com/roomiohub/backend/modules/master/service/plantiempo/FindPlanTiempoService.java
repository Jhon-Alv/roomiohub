package com.roomiohub.backend.modules.master.service.plantiempo;

import com.roomiohub.backend.modules.master.dto.plantiempo.response.FindPlanTiempoResponse;
import com.roomiohub.backend.modules.master.mapper.PlanTiempoMapper;
import com.roomiohub.backend.modules.master.model.PlanTiempoEntity;
import com.roomiohub.backend.modules.master.repository.PlanTiempoRepository;
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
public class FindPlanTiempoService {

    private final PlanTiempoRepository planTiempoRepository;
    private final PlanTiempoMapper planTiempoMapper;

    @Transactional(readOnly = true)
    public FindPlanTiempoResponse execute(UUID planTiempoId){
        log.info("Obteniendo plan de tiempo con ID: {}", planTiempoId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        PlanTiempoEntity planTiempo = planTiempoRepository.findByIdAndTenantId(planTiempoId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Plan de tiempo no encontrado con ID: " + planTiempoId));

        log.info("Plan de tiempo encontrado: {} - {}", planTiempo.getId(), planTiempo.getNombre());

        return planTiempoMapper.toFindResponse(planTiempo);
    }
}
