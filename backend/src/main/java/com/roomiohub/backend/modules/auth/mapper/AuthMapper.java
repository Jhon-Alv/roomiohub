package com.roomiohub.backend.modules.auth.mapper;

import com.roomiohub.backend.modules.auth.dto.request.RegisterRequest;
import com.roomiohub.backend.modules.auth.dto.response.LoginResponse;
import com.roomiohub.backend.modules.auth.dto.request.LoginRequest;
import com.roomiohub.backend.modules.auth.dto.response.RegisterResponse;
import com.roomiohub.backend.security.identity.model.CuentaEntity;
import com.roomiohub.backend.security.identity.model.PlanEntity;
import com.roomiohub.backend.security.identity.model.RolUsuario;
import com.roomiohub.backend.security.identity.model.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuthMapper {

    LoginResponse toLoginResponse(UsuarioEntity user, String token);

    default UsernamePasswordAuthenticationToken toAuthToken(LoginRequest request) {
        if (request == null) {
            return null;
        }
        String combinedUsername = request.getRuc() + ":" + request.getUsername();
        return new UsernamePasswordAuthenticationToken(
                combinedUsername,
                request.getPassword()
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plan", source = "plan")
    @Mapping(target = "nombreComercial", source = "request.nombreComercial")
    @Mapping(target = "ruc", source = "request.ruc")
    CuentaEntity toCuenta(RegisterRequest request, PlanEntity plan);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "rol", source = "rol")
    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "nombre", source = "request.nombre")
    UsuarioEntity toUsuario(RegisterRequest request, String encodedPassword, RolUsuario rol);

    @Mapping(target = "passwordTemporal", source = "rawPassword")
    @Mapping(target = "username", source = "usuario.username")
    RegisterResponse toRegisterResponse(UsuarioEntity usuario, String rawPassword);

}
