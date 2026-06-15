package com.roomiohub.backend.modules.master.service.catalog;

import com.roomiohub.backend.modules.master.dto.catalog.OptionDto;
import com.roomiohub.backend.modules.master.repository.PlanTiempoRepository;
import com.roomiohub.backend.modules.master.repository.PlanTiempoSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlanesTiempoCatalogService {

    private final PlanTiempoRepository planTiempoRepository;

    @Transactional(readOnly = true)
    public List<OptionDto> execute() {
        log.info("Obteniendo catalogo de planes de tiempo");
        return planTiempoRepository.findAll(PlanTiempoSpecification.getFiltros(null, true)).stream()
                .map(plan -> OptionDto.builder()
                        .id(plan.getId())
                        .label(plan.getNombre())
                        .build())
                .collect(Collectors.toList());
    }
}
