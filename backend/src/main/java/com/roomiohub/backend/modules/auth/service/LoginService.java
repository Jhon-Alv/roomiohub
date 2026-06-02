package com.roomiohub.backend.modules.auth.service;

import com.roomiohub.backend.modules.auth.dto.response.LoginResponse;
import com.roomiohub.backend.modules.auth.dto.request.LoginRequest;
import com.roomiohub.backend.modules.auth.mapper.AuthMapper;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
import com.roomiohub.backend.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final AuthMapper authMapper;


    public LoginResponse execute(LoginRequest request) {

        log.info("Intentando autenticar al usuario: {}", request.getUsername());

        UsernamePasswordAuthenticationToken authToken = authMapper.toAuthToken(request);

        authenticationManager.authenticate(authToken);

        UsuarioEntity user = usuarioRepository.findByUsernameAndActivoTrue(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado o inactivo"));

        String token = jwtService.generateToken(user);

        log.info("Usuario {} autenticado exitosamente.", user.getUsername());

        return authMapper.toLoginResponse(user, token);

    }

}
