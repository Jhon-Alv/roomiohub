package com.roomiohub.backend.modules.master.service.catalog;

import com.roomiohub.backend.modules.master.dto.catalog.OptionDto;
import com.roomiohub.backend.modules.master.repository.PisoRepository;
import com.roomiohub.backend.modules.master.repository.PisoSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PisoCatalogService {

    private final PisoRepository pisoRepository;

    @Transactional(readOnly = true)
    public List<OptionDto> execute(UUID sedeId) {
        log.info("Obteniendo catalogo de pisos para la sede: {}", sedeId);
        return pisoRepository.findAll(PisoSpecification.getFiltros(null, true, sedeId)).stream()
                .map(piso -> OptionDto.builder()
                        .id(piso.getId())
                        .label(piso.getNombre())
                        .build())
                .collect(Collectors.toList());
    }
}
