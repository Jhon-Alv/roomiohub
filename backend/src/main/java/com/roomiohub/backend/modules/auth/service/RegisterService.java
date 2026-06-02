package com.roomiohub.backend.modules.auth.service;

import com.roomiohub.backend.modules.auth.dto.request.RegisterRequest;
import com.roomiohub.backend.modules.auth.dto.response.RegisterResponse;
import com.roomiohub.backend.modules.auth.mapper.AuthMapper;
import com.roomiohub.backend.modules.auth.util.PasswordGenerator;
import com.roomiohub.backend.security.identity.model.CuentaEntity;
import com.roomiohub.backend.security.identity.model.PlanEntity;
import com.roomiohub.backend.security.identity.model.RolUsuario;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import com.roomiohub.backend.security.identity.repository.CuentaRepository;
import com.roomiohub.backend.security.identity.repository.PlanRepository;
import com.roomiohub.backend.security.identity.repository.UsuarioRepository;
import com.roomiohub.backend.shared.exceptions.RestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterService {

    private final CuentaRepository cuentaRepository;
    private final PlanRepository planRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final PasswordGenerator passwordGenerator;

    @Transactional
    public RegisterResponse execute(RegisterRequest request) {

        log.info("Iniciando registro de empresa: {}", request.getNombreComercial());

        if (cuentaRepository.existsByRuc(request.getRuc())) {
            throw new RestException(HttpStatus.CONFLICT, "Ya existe una cuenta con el mismo RUC");
        }

        UUID planId = UUID.fromString(request.getPlanId());

        PlanEntity planSeleccionado = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan no encontrado"));

        CuentaEntity nuevaCuenta = authMapper.toCuenta(request, planSeleccionado);
        LocalDateTime fechaInicioSuscripcion = LocalDateTime.now();
        LocalDateTime fechaFinSuscripcion = fechaInicioSuscripcion.plusMonths(1L);

        nuevaCuenta.setFechaInicioSuscripcion(fechaInicioSuscripcion);
        nuevaCuenta.setFechaFinSuscripcion(fechaFinSuscripcion);

        cuentaRepository.persist(nuevaCuenta);

        String rawPassword = passwordGenerator.generate(10);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        UsuarioEntity nuevoAdmin = authMapper.toUsuario(request, encodedPassword, RolUsuario.ADMIN);
        nuevoAdmin.setTenantId(nuevaCuenta.getId());

        UsuarioEntity usuarioGuardado = usuarioRepository.persist(nuevoAdmin);

        return authMapper.toRegisterResponse(usuarioGuardado, rawPassword);

    }

}
