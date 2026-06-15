package com.roomiohub.backend.modules.master.service.categoria;

import com.roomiohub.backend.modules.master.dto.categoria.response.FindCategoriaResponse;
import com.roomiohub.backend.modules.master.mapper.CategoriaMapper;
import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import com.roomiohub.backend.modules.master.repository.CategoriaRepository;
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
public class FindCategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Transactional(readOnly = true)
    public FindCategoriaResponse execute(UUID categoriaId){
        log.info("Obteniendo categoría con ID: {}", categoriaId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        CategoriaEntity categoria = categoriaRepository.findByIdAndTenantId(categoriaId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + categoriaId));

        log.info("Categoría encontrada: {} - {}", categoria.getId(), categoria.getNombre());

        return categoriaMapper.toFindCategoriaResponse(categoria);
    }
}
