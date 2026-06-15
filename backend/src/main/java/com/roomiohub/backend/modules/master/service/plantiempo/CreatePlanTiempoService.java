package com.roomiohub.backend.modules.master.service.plantiempo;

import com.roomiohub.backend.modules.master.dto.plantiempo.request.CreatePlanTiempoRequest;
import com.roomiohub.backend.modules.master.dto.plantiempo.response.CreatePlanTiempoResponse;
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
public class CreatePlanTiempoService {

    private final PlanTiempoRepository planTiempoRepository;
    private final PlanTiempoMapper planTiempoMapper;

    @Transactional
    public CreatePlanTiempoResponse execute(CreatePlanTiempoRequest request) {
        log.info("Iniciando proceso de creación de plan de tiempo con nombre: {}", request.getNombre());

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        if (planTiempoRepository.existsByNombreAndTenantId(request.getNombre(), tenantId)) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe un plan de tiempo con el nombre especificado");
        }

        PlanTiempoEntity planTiempo = planTiempoMapper.toCreateEntity(request);
        planTiempo.setTenantId(tenantId);

        PlanTiempoEntity savedPlanTiempo = planTiempoRepository.persist(planTiempo);
        log.info("Creación de plan de tiempo con ID: {}", savedPlanTiempo.getId());

        return planTiempoMapper.toCreateResponse(savedPlanTiempo);
    }
}
