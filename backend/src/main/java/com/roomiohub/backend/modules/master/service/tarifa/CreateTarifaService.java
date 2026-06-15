package com.roomiohub.backend.modules.master.service.tarifa;

import com.roomiohub.backend.modules.master.dto.tarifa.request.CreateTarifaRequest;
import com.roomiohub.backend.modules.master.dto.tarifa.response.CreateTarifaResponse;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTarifaService {

    private final TarifaRepository tarifaRepository;
    private final CategoriaRepository categoriaRepository;
    private final PlanTiempoRepository planTiempoRepository;
    private final TarifaMapper tarifaMapper;

    @Transactional
    public CreateTarifaResponse execute(CreateTarifaRequest request) {
        log.info("Iniciando proceso de creación de tarifa para categoría {} y plan de tiempo {}", request.getCategoriaId(), request.getPlanTiempoId());

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        if (tarifaRepository.existsByCategoriaIdAndPlanTiempoIdAndTenantId(request.getCategoriaId(), request.getPlanTiempoId(), tenantId)) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una tarifa para la combinación de esta categoría y plan de tiempo");
        }

        CategoriaEntity categoria = categoriaRepository.findByIdAndTenantId(request.getCategoriaId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Categoría no encontrada o no pertenece al tenant actual"));

        PlanTiempoEntity planTiempo = planTiempoRepository.findByIdAndTenantId(request.getPlanTiempoId(), tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Plan de tiempo no encontrado o no pertenece al tenant actual"));

        TarifaEntity tarifa = tarifaMapper.toCreateEntity(request);
        tarifa.setCategoria(categoria);
        tarifa.setPlanTiempo(planTiempo);
        tarifa.setTenantId(tenantId);

        TarifaEntity savedTarifa = tarifaRepository.persist(tarifa);
        log.info("Creación de tarifa con ID: {}", savedTarifa.getId());

        return tarifaMapper.toCreateResponse(savedTarifa);
    }
}
