package com.roomiohub.backend.modules.admin.dto.usuario.request;

import com.roomiohub.backend.security.identity.model.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder los '{max}' caracteres")
    private String nombre;

    @NotBlank(message = "El username es obligatorio")
    @Size(max = 100, message = "El username no puede exceder los '{max}' caracteres")
    private String username;

    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol;

    @Size(max = 64, message = "La nueva contraseña no puede exceder los '{max}' caracteres")
    private String nuevaPassword;

}
