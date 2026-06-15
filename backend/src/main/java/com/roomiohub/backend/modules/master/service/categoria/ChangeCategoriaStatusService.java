package com.roomiohub.backend.modules.master.service.categoria;

import com.roomiohub.backend.modules.master.dto.categoria.request.ChangeCategoriaStatusRequest;
import com.roomiohub.backend.modules.master.model.CategoriaEntity;
import com.roomiohub.backend.modules.master.repository.CategoriaRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangeCategoriaStatusService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public void execute(UUID categoriaId, ChangeCategoriaStatusRequest request) {
        log.info("Cambiando estado de la categoría con ID: {}", categoriaId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        CategoriaEntity categoria = categoriaRepository.findByIdAndTenantId(categoriaId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Categoría no existe o no tiene acceso"));

        if (Objects.equals(categoria.getActivo(), request.getActivo())) {
            log.warn("La categoría con ID: {} ya se encuentra en estado: {}", categoriaId, request.getActivo());
            return;
        }

        categoria.setActivo(request.getActivo());
        categoriaRepository.merge(categoria);

        log.info("Estado de la categoría con ID: {} cambiado a: {}", categoriaId, request.getActivo());
    }
}
