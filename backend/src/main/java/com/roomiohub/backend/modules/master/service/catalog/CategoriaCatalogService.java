package com.roomiohub.backend.modules.master.service.catalog;

import com.roomiohub.backend.modules.master.dto.catalog.OptionDto;
import com.roomiohub.backend.modules.master.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoriaCatalogService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<OptionDto> execute() {
        log.info("Obteniendo catalogo de categorias");
        return categoriaRepository.findAllByActivoTrue().stream()
                .map(categoria -> OptionDto.builder()
                        .id(categoria.getId())
                        .label(categoria.getNombre())
                        .build())
                .collect(Collectors.toList());
    }
}
