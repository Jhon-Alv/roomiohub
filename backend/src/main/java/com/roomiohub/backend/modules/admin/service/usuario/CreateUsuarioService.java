package com.roomiohub.backend.modules.admin.service.usuario;

import com.roomiohub.backend.modules.admin.dto.usuario.request.CreateUsuarioRequest;
import com.roomiohub.backend.modules.admin.dto.usuario.response.CreateUsuarioResponse;
import com.roomiohub.backend.modules.admin.mapper.UsuarioMapper;
import com.roomiohub.backend.modules.admin.validation.CreateUsuarioValidationService;
import com.roomiohub.backend.modules.auth.util.PasswordGenerator;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final CreateUsuarioValidationService validationService;

    @Transactional
    public CreateUsuarioResponse execute(CreateUsuarioRequest request) {
        log.info("Iniciando proceso de creación de usuario con username: {}", request.getUsername());
        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        String temporalPassword = passwordGenerator.generate(10);
        String encodedPassword = passwordEncoder.encode(temporalPassword);

        UsuarioEntity usuario = usuarioMapper.toCreateUsuarioEntity(request, encodedPassword);
        usuario.setTenantId(tenantId);

        validationService.validate(usuario);

        UsuarioEntity savedUsuario = usuarioRepository.persist(usuario);
        log.info("Creacion de usuario con ID: {}", savedUsuario.getId());

        return usuarioMapper.toCreateUsuarioResponse(savedUsuario, temporalPassword);
    }
}
