package com.roomiohub.backend.modules.admin.service.usuario;

import com.roomiohub.backend.modules.admin.dto.usuario.request.ChangeUsuarioStatusRequest;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
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
public class ChangeUsuarioStatusService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public void execute(UUID usuarioId, ChangeUsuarioStatusRequest request) {

        log.info("Cambiando estado del usuario con ID: {}", usuarioId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        UsuarioEntity usuario = usuarioRepository.findByIdAndTenantId(usuarioId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND ,"Usuario no existe o no tiene acceso"));

        if (Objects.equals(usuario.getActivo(), request.getActivo())) {
            log.warn("El usuario con ID: {} ya se encuentra en estado: {}", usuarioId, request.getActivo());
            return;
        }

        usuario.setActivo(request.getActivo());
        usuarioRepository.merge(usuario);

        log.info("Estado del usuario con ID: {} cambiado a: {}", usuarioId, request.getActivo());

    }


}
