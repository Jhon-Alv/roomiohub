package com.roomiohub.backend.modules.master.service.plantiempo;

import com.roomiohub.backend.modules.master.dto.plantiempo.request.UpdatePlanTiempoRequest;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatePlanTiempoService {

    private final PlanTiempoRepository planTiempoRepository;
    private final PlanTiempoMapper planTiempoMapper;

    @Transactional
    public void execute(UUID id, UpdatePlanTiempoRequest request) {
        log.info("Iniciando proceso de actualización de plan de tiempo con ID: {}", id);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        PlanTiempoEntity planTiempo = planTiempoRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Plan de tiempo no existe o no tiene acceso"));

        if (!planTiempo.getNombre().equals(request.getNombre()) && planTiempoRepository.existsByNombreAndTenantId(request.getNombre(), tenantId)) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe un plan de tiempo con ese nombre");
        }

        planTiempoMapper.toUpdateEntity(planTiempo, request);

        planTiempoRepository.merge(planTiempo);

        log.info("Plan de tiempo con ID: {} actualizado exitosamente", id);
    }
}
