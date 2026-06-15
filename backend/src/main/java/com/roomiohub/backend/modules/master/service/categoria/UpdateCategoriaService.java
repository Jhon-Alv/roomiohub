package com.roomiohub.backend.modules.master.service.categoria;

import com.roomiohub.backend.modules.master.dto.categoria.request.UpdateCategoriaRequest;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Transactional
    public void execute(UUID id, UpdateCategoriaRequest request) {
        log.info("Iniciando proceso de actualización de categoría con ID: {}", id);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        CategoriaEntity categoria = categoriaRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Categoría no existe o no tiene acceso"));

        if (!categoria.getNombre().equals(request.getNombre()) && categoriaRepository.existsByNombreAndTenantId(request.getNombre(), tenantId)) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una categoría con ese nombre");
        }

        categoriaMapper.toUpdateCategoriaEntity(categoria, request);

        categoriaRepository.merge(categoria);

        log.info("Categoría con ID: {} actualizada exitosamente", id);
    }
}
