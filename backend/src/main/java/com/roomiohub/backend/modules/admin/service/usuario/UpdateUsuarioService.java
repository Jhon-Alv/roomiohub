package com.roomiohub.backend.modules.admin.service.usuario;

import com.roomiohub.backend.modules.admin.dto.usuario.request.UpdateUsuarioRequest;
import com.roomiohub.backend.modules.admin.mapper.UsuarioMapper;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void execute(UUID id, UpdateUsuarioRequest request) {

        log.info("Iniciando proceso de actualización de usuario con ID: {}", id);

        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "Usuario no existe o no tiene acceso"));

        if (request.getNuevaPassword() != null) {
            String encodedPassword = passwordEncoder.encode(request.getNuevaPassword());
            request.setNuevaPassword(encodedPassword);
        }

        usuarioMapper.toUpdateUsuarioEntity(usuario, request);

        usuarioRepository.merge(usuario);

        log.info("Usuario con ID: {} actualizado exitosamente", id);

    }



}
