package com.roomiohub.backend.modules.master.service.plantiempo;

import com.roomiohub.backend.modules.master.dto.plantiempo.request.ChangePlanTiempoStatusRequest;
import com.roomiohub.backend.modules.master.model.PlanTiempoEntity;
import com.roomiohub.backend.modules.master.repository.PlanTiempoRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangePlanTiempoStatusService {

    private final PlanTiempoRepository planTiempoRepository;

    @Transactional
    public void execute(UUID planTiempoId, ChangePlanTiempoStatusRequest request) {
        log.info("Cambiando estado del plan de tiempo con ID: {}", planTiempoId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        PlanTiempoEntity planTiempo = planTiempoRepository.findByIdAndTenantId(planTiempoId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Plan de tiempo no existe o no tiene acceso"));

        if (Objects.equals(planTiempo.getActivo(), request.getActivo())) {
            log.warn("El plan de tiempo con ID: {} ya se encuentra en estado: {}", planTiempoId, request.getActivo());
            return;
        }

        planTiempo.setActivo(request.getActivo());
        planTiempoRepository.merge(planTiempo);

        log.info("Estado del plan de tiempo con ID: {} cambiado a: {}", planTiempoId, request.getActivo());
    }
}
