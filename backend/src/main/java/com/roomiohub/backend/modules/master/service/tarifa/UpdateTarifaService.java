package com.roomiohub.backend.modules.master.service.tarifa;

import com.roomiohub.backend.modules.master.dto.tarifa.request.UpdateTarifaRequest;
import com.roomiohub.backend.modules.master.mapper.TarifaMapper;
import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import com.roomiohub.backend.modules.master.model.PlanTiempoEntity;
import com.roomiohub.backend.modules.master.model.TarifaEntity;
import com.roomiohub.backend.modules.master.repository.CategoriaRepository;
import com.roomiohub.backend.modules.master.repository.PlanTiempoRepository;
import com.roomiohub.backend.modules.master.repository.TarifaRepository;
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
public class UpdateTarifaService {

    private final TarifaRepository tarifaRepository;
    private final CategoriaRepository categoriaRepository;
    private final PlanTiempoRepository planTiempoRepository;
    private final TarifaMapper tarifaMapper;

    @Transactional
    public void execute(UUID id, UpdateTarifaRequest request) {
        log.info("Iniciando proceso de actualización de tarifa con ID: {}", id);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        TarifaEntity tarifa = tarifaRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Tarifa no existe o no tiene acceso"));

        boolean combiCambiada = !tarifa.getCategoria().getId().equals(request.getCategoriaId()) || 
                                !tarifa.getPlanTiempo().getId().equals(request.getPlanTiempoId());

        if (combiCambiada && tarifaRepository.existsByCategoriaIdAndPlanTiempoIdAndTenantId(request.getCategoriaId(), request.getPlanTiempoId(), tenantId)) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una tarifa con esta combinación de categoría y plan de tiempo");
        }

        CategoriaEntity categoria = categoriaRepository.findByIdAndTenantId(request.getCategoriaId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Categoría no encontrada o no pertenece al tenant actual"));

        PlanTiempoEntity planTiempo = planTiempoRepository.findByIdAndTenantId(request.getPlanTiempoId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Plan de tiempo no encontrado o no pertenece al tenant actual"));

        tarifaMapper.toUpdateEntity(tarifa, request);
        tarifa.setCategoria(categoria);
        tarifa.setPlanTiempo(planTiempo);

        tarifaRepository.merge(tarifa);

        log.info("Tarifa con ID: {} actualizada exitosamente", id);
    }
}
