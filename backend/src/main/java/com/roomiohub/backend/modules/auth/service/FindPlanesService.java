package com.roomiohub.backend.modules.auth.service;

import com.roomiohub.backend.modules.auth.dto.response.PlanResponse;
import com.roomiohub.backend.security.identity.model.PlanEntity;
import com.roomiohub.backend.security.identity.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindPlanesService {

    private final PlanRepository planRepository;

    @Transactional(readOnly = true)
    public List<PlanResponse> execute() {
        log.info("Consultando catalogo de planes activos");

        List<PlanEntity> planes = planRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .toList();

        return planes.stream()
                .map(this::toResponse)
                .toList();
    }

    private PlanResponse toResponse(PlanEntity entity) {
        return PlanResponse.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .precioMensual(entity.getPrecioMensual())
                .maxHabitaciones(entity.getMaxHabitaciones())
                .maxUsuarios(entity.getMaxUsuarios())
                .maxSedes(entity.getMaxSedes())
                .flagModuloPromociones(entity.getFlagModuloPromociones())
                .build();
    }

}
