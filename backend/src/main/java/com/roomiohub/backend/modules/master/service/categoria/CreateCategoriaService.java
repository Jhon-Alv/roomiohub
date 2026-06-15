package com.roomiohub.backend.modules.master.service.categoria;

import com.roomiohub.backend.modules.master.dto.categoria.request.CreateCategoriaRequest;
import com.roomiohub.backend.modules.master.dto.categoria.response.CreateCategoriaResponse;
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
public class CreateCategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Transactional
    public CreateCategoriaResponse execute(CreateCategoriaRequest request) {
        log.info("Iniciando proceso de creación de categoría con nombre: {}", request.getNombre());

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        if (categoriaRepository.existsByNombreAndTenantId(request.getNombre(), tenantId)) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una categoría con el nombre especificado");
        }

        CategoriaEntity categoria = categoriaMapper.toCreateCategoriaEntity(request);
        categoria.setTenantId(tenantId);

        CategoriaEntity savedCategoria = categoriaRepository.persist(categoria);
        log.info("Creación de categoría con ID: {}", savedCategoria.getId());

        return categoriaMapper.toCreateCategoriaResponse(savedCategoria);
    }
}
