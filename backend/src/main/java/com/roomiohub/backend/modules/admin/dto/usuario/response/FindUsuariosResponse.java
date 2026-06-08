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
public class FindUsuariosResponse {

    private UUID id;
    private String username;
    private String nombre;
    private RolUsuario rol;
    private Boolean activo;

}
