package com.roomiohub.backend.modules.admin.service.usuario;

import com.roomiohub.backend.modules.admin.dto.usuario.response.FindUsuarioResponse;
import com.roomiohub.backend.modules.admin.mapper.UsuarioMapper;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public FindUsuarioResponse execute(UUID usuarioId){

        log.info("Obteniendo usuario con ID: {}", usuarioId);

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        UsuarioEntity usuario = usuarioRepository.findByIdAndTenantId(usuarioId, tenantId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + usuarioId));

        log.info("Usuario encontrado: {} - {}", usuario.getId(), usuario.getUsername());

        return usuarioMapper.toFindUsuarioResponse(usuario);

    }

}
