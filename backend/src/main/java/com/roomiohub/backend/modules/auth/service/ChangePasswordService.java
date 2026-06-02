package com.roomiohub.backend.modules.auth.service;

import com.roomiohub.backend.modules.auth.dto.request.ChangePasswordRequest;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChangePasswordService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void execute(ChangePasswordRequest request){

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RestException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        UsuarioEntity usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usuario.setFlagCambiarPassword(Boolean.FALSE);

        usuarioRepository.merge(usuario);

        log.info("Contraseña cambiada exitosamente para el usuario: {}", username);

    }

}
