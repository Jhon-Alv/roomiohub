package com.roomiohub.backend.modules.admin.validation;

import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
import com.roomiohub.backend.security.tenant.TenantContextHolder;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateUsuarioValidationService {

    private final UsuarioRepository usuarioRepository;

    public void validate(UsuarioEntity usuario) {

        UUID tenantId = TenantContextHolder.getRequiredTenantId();

        if (usuarioRepository.existsByUsernameAndTenantId(usuario.getUsername(), tenantId)) {
            throw new RestException(HttpStatus.CONFLICT, "El username ya se encuentra registrado");
        }

    }
}
