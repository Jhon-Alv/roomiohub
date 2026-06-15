package com.roomiohub.backend.modules.master.service.categoria;

import com.roomiohub.backend.modules.master.dto.categoria.response.FindCategoriasResponse;
import com.roomiohub.backend.modules.master.mapper.CategoriaMapper;
import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import com.roomiohub.backend.modules.master.repository.CategoriaRepository;
import com.roomiohub.backend.modules.master.repository.CategoriaSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindCategoriasService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Transactional(readOnly = true)
    public List<FindCategoriasResponse> execute(String search, Boolean activo) {
        log.info("Obteniendo lista de categorías con filtros: search={}, activo={}", search, activo);

        Specification<CategoriaEntity> spec = CategoriaSpecification.getFiltros(search, activo);

        List<CategoriaEntity> categorias = categoriaRepository.findAll(spec);

        return categoriaMapper.toFindCategoriasResponseList(categorias);
    }
}
