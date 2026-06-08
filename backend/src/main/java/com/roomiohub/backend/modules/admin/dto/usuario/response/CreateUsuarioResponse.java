package com.roomiohub.backend.modules.admin.dto.usuario.response;

import com.roomiohub.backend.security.identity.model.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUsuarioResponse {
    private UUID id;
    private String nombre;
    private String username;
    private String temporalPassword;
    private RolUsuario rol;
    private UUID sedeId;
}
